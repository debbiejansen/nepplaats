package nl.novi.nepplaats.service;


import nl.novi.nepplaats.dto.gebruiker.GebruikerDto;
import nl.novi.nepplaats.model.Gebruiker;
import nl.novi.nepplaats.repository.GebruikerRepository;
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
    public List<GebruikerDto> getAllGebruikers() {
        List<Gebruiker> gebruikers = gebruikerRepository.findAll();
        List<GebruikerDto> gebruikerDtos = new ArrayList<>();

        for (Gebruiker gebruiker : gebruikers) {
            gebruikerDtos.add(transferToDto(gebruiker));
        }

        return gebruikerDtos;
    }

    // Maak een nieuwe gebruiker aan
    public GebruikerDto createGebruiker(GebruikerDto gebruikerDto) {
        // Zet DTO om naar Entiteit
        Gebruiker gebruiker = transferToEntity(gebruikerDto);

        // Sla de entiteit op in de database via de repository
        Gebruiker savedGebruiker = gebruikerRepository.save(gebruiker);

        // Zet het opgeslagen resultaat weer om naar een DTO om terug te sturen
        return transferToDto(savedGebruiker);
    }

    // Helper methode: DTO -> Entiteit
    private Gebruiker transferToEntity(GebruikerDto dto) {
        Gebruiker gebruiker = new Gebruiker();
        gebruiker.setGebruikersnaam(dto.getGebruikersnaam());
        gebruiker.setEmail(dto.getEmail());
        gebruiker.setWachtwoord(dto.getWachtwoord());
        gebruiker.setRolId(dto.getRolId());
        gebruiker.setBeschrijving(dto.getBeschrijving());
        return gebruiker;
    }

    // Helper methode: Entiteit -> DTO
    private GebruikerDto transferToDto(Gebruiker gebruiker) {
        GebruikerDto dto = new GebruikerDto();
        dto.setGebruikerId(gebruiker.getGebruikerId());
        dto.setGebruikersnaam(gebruiker.getGebruikersnaam());
        dto.setEmail(gebruiker.getEmail());
        dto.setWachtwoord(gebruiker.getWachtwoord());
        dto.setRolId(gebruiker.getRolId());
        dto.setBeschrijving(gebruiker.getBeschrijving());
        return dto;
    }
    

}