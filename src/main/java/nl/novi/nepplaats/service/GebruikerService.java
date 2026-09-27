package nl.novi.nepplaats.service;


import nl.novi.nepplaats.dto.gebruiker.GebruikerDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.Gebruiker;
import nl.novi.nepplaats.repository.GebruikerRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GebruikerService {


    private final GebruikerRepository gebruikerRepository;

    public GebruikerService(GebruikerRepository gebruikerRepository) {
        this.gebruikerRepository = gebruikerRepository;
    }

    // Haal alle gebruiker op en zet ze om naar DTO's
    public List<GebruikerDto.Response> getAllGebruikers() {
        List<Gebruiker> gebruikers = gebruikerRepository.findAll();
        List<GebruikerDto.Response> gebruikerDtos = new ArrayList<>();

        for (Gebruiker gebruiker : gebruikers) {
            gebruikerDtos.add(transferToDto(gebruiker));
        }

        return gebruikerDtos;
    }

    // Haal één gebruiker op basis van ID
    public GebruikerDto.Response getGebruikerById(Long id) {
        Gebruiker gebruiker = gebruikerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Gebruiker niet gevonden met id: " + id));

        return transferToDto(gebruiker);
    }

    /**
     * Zoekt de gebruiker op basis van het Keycloak ID (sub claim uit JWT).
     * Als de gebruiker nog niet bestaat in de PostgreSQL database, wordt deze automatisch aangemaakt.
     */
    public Gebruiker getOrCreateGebruikerFromJwt(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // 'sub' claim uit JWT (unieke UUID van Keycloak)
        Long berekendeRolId = bepaalRolIdUitJwt(jwt);
        // 1. Zoek bestaande gebruiker OF maak een nieuwe aan
        Gebruiker gebruiker = gebruikerRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    // Gebruiker bestaat nog niet in onze database -> Automatisch profiel aanmaken!
                    Gebruiker nieuweGebruiker = new Gebruiker();
                    nieuweGebruiker.setKeycloakId(keycloakId);

                    // Haal claims op uit de JWT (Keycloak levert deze standaard mee)
                    String email = jwt.getClaimAsString("email");
                    String username = jwt.getClaimAsString("preferred_username");

                    nieuweGebruiker.setEmail(email != null ? email : "onbekend@keycloak.com");
                    nieuweGebruiker.setGebruikersnaam(username != null ? username : keycloakId);
                    nieuweGebruiker.setBeschrijving("Nieuwe gebruiker via Keycloak");

                    // Bepaal rol_id: 1 voor ADMIN, 2 voor USER
                    nieuweGebruiker.setRolId(bepaalRolIdUitJwt(jwt));
                    return gebruikerRepository.save(nieuweGebruiker);
                });
        // 2. Zorg dat de rol_id ALTIJD (ook bij bestaande gebruikers) geüpdatet wordt als deze verschilt
        if (gebruiker.getRolId() == null || !gebruiker.getRolId().equals(berekendeRolId)) {
            gebruiker.setRolId(berekendeRolId);
            return gebruikerRepository.save(gebruiker);
        }

        return gebruiker;
    }

    /**
     * Helper methode om veilig de rollen uit de geneste Map van de JWT te lezen.
     */
    @SuppressWarnings("unchecked")
    private Long bepaalRolIdUitJwt(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess != null && resourceAccess.get("nepplaats-backend") instanceof Map) {
            Map<String, Object> client = (Map<String, Object>) resourceAccess.get("nepplaats-backend");
            if (client != null && client.containsKey("roles")) {
                List<String> roles = (List<String>) client.get("roles");
                if (roles != null && (roles.contains("ADMIN") || roles.contains("ROLE_ADMIN"))) {
                    return 1L; // ADMIN
                }
            }
        }

        return 2L; // Standard USER
    }

    // Maak een nieuwe gebruiker aan
    public GebruikerDto.Response createGebruiker(GebruikerDto.Request gebruikerDto) {
        Gebruiker gebruiker = transferToEntity(gebruikerDto);
        Gebruiker savedGebruiker = gebruikerRepository.save(gebruiker);
        return transferToDto(savedGebruiker);
    }

    // Wijzig een bestaande gebruiker
    public GebruikerDto.Response updateGebruiker(Long id, GebruikerDto.Request gebruikerDto) {
        Gebruiker bestaandeGebruiker = gebruikerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Gebruiker niet gevonden met id: " + id));

        // Werk de velden bij
        bestaandeGebruiker.setGebruikersnaam(gebruikerDto.getGebruikersnaam());
        bestaandeGebruiker.setEmail(gebruikerDto.getEmail());
        bestaandeGebruiker.setRolId(gebruikerDto.getRolId());
        bestaandeGebruiker.setBeschrijving(gebruikerDto.getBeschrijving());

        Gebruiker gewijzigdeGebruiker = gebruikerRepository.save(bestaandeGebruiker);
        return transferToDto(gewijzigdeGebruiker);
    }

    // Verwijder een gebruiker op basis van ID
    public void deleteGebruiker(Long id) {
        if (!gebruikerRepository.existsById(id)) {
            throw new RecordNotFoundException("Gebruiker niet gevonden met id: " + id);
        }
        gebruikerRepository.deleteById(id);
    }

    // Helper methode: DTO -> Entiteit
    private Gebruiker transferToEntity(GebruikerDto.Request dto) {
        Gebruiker gebruiker = new Gebruiker();
        gebruiker.setGebruikersnaam(dto.getGebruikersnaam());
        gebruiker.setEmail(dto.getEmail());
        gebruiker.setRolId(dto.getRolId());
        gebruiker.setBeschrijving(dto.getBeschrijving());
        return gebruiker;
    }

    // Helper methode: Entiteit -> DTO
    private GebruikerDto.Response transferToDto(Gebruiker gebruiker) {
        GebruikerDto.Response dto = new GebruikerDto.Response();
        dto.setGebruikerId(gebruiker.getGebruikerId());
        dto.setGebruikersnaam(gebruiker.getGebruikersnaam());
        dto.setEmail(gebruiker.getEmail());
        dto.setRolId(gebruiker.getRolId());
        dto.setBeschrijving(gebruiker.getBeschrijving());
        return dto;
    }
}