package nl.novi.nepplaats.service;


import nl.novi.nepplaats.dto.gebruiker.GebruikerDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.Gebruiker;
import nl.novi.nepplaats.model.Rol;
import nl.novi.nepplaats.repository.GebruikerRepository;
import nl.novi.nepplaats.repository.RolRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GebruikerService {

    private final GebruikerRepository gebruikerRepository;
    private final RolRepository rolRepository;

    public GebruikerService(GebruikerRepository gebruikerRepository, RolRepository rolRepository) {
        this.gebruikerRepository = gebruikerRepository;
        this.rolRepository = rolRepository;
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
        String keycloakId = jwt.getSubject();
        Long berekendeRolId = bepaalRolIdUitJwt(jwt);

        // Fetch the corresponding Rol entity from DB
        Rol berekendeRol = rolRepository.findById(berekendeRolId)
                .orElseThrow(() -> new RecordNotFoundException("Rol niet gevonden voor ID: " + berekendeRolId));

        // 1. Zoek bestaande gebruiker OF maak een nieuwe aan
        Gebruiker gebruiker = gebruikerRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    Gebruiker nieuweGebruiker = new Gebruiker();
                    nieuweGebruiker.setKeycloakId(keycloakId);

                    String email = jwt.getClaimAsString("email");
                    String username = jwt.getClaimAsString("preferred_username");

                    nieuweGebruiker.setEmail(email != null ? email : "onbekend@keycloak.com");
                    nieuweGebruiker.setGebruikersnaam(username != null ? username : keycloakId);
                    nieuweGebruiker.setBeschrijving("Nieuwe gebruiker via Keycloak");

                    // Set the Rol entity reference instead of a raw Long
                    nieuweGebruiker.setRol(berekendeRol);
                    return gebruikerRepository.save(nieuweGebruiker);
                });

        // 2. Zorg dat de rol ALTIJD geüpdatet wordt als deze verschilt
        if (gebruiker.getRol() == null || !gebruiker.getRol().getRolId().equals(berekendeRolId)) {
            gebruiker.setRol(berekendeRol);
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

        Rol rol = rolRepository.findById(gebruikerDto.getRolId())
                .orElseThrow(() -> new RecordNotFoundException("Rol niet gevonden met id: " + gebruikerDto.getRolId()));

        // Werk de velden bij
        bestaandeGebruiker.setGebruikersnaam(gebruikerDto.getGebruikersnaam());
        bestaandeGebruiker.setEmail(gebruikerDto.getEmail());
        bestaandeGebruiker.setRol(rol);
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
        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RecordNotFoundException("Rol niet gevonden met id: " + dto.getRolId()));
        gebruiker.setGebruikersnaam(dto.getGebruikersnaam());
        gebruiker.setEmail(dto.getEmail());
        gebruiker.setRol(rol);
        gebruiker.setBeschrijving(dto.getBeschrijving());
        return gebruiker;
    }

    // Helper methode: Entiteit -> DTO
    private GebruikerDto.Response transferToDto(Gebruiker gebruiker) {
        GebruikerDto.Response dto = new GebruikerDto.Response();
        dto.setGebruikerId(gebruiker.getGebruikerId());
        dto.setGebruikersnaam(gebruiker.getGebruikersnaam());
        dto.setEmail(gebruiker.getEmail());
        dto.setRolId(gebruiker.getRol().getRolId());
        dto.setBeschrijving(gebruiker.getBeschrijving());
        return dto;
    }
}