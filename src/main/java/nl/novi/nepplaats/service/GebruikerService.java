package nl.novi.nepplaats.service;


import nl.novi.nepplaats.dto.gebruiker.GebruikerDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.Gebruiker;
import nl.novi.nepplaats.repository.GebruikerRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        return gebruikerRepository.findByKeycloakId(keycloakId)
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

                    return gebruikerRepository.save(nieuweGebruiker);
                });
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