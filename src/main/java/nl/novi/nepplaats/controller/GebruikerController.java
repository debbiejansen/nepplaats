package nl.novi.nepplaats.controller;

import nl.novi.nepplaats.dto.gebruiker.GebruikerDto;
import nl.novi.nepplaats.service.GebruikerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/gebruikers")
public class GebruikerController {

    private final GebruikerService gebruikerService;

    // Injecteer de Service via de constructor
    public GebruikerController(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    // POST: Nieuwe gebruiker toevoegen
    @PostMapping("/create")
    public ResponseEntity<GebruikerDto> createRol(@RequestBody GebruikerDto newGebruikerDto) {
        GebruikerDto createdGebruiker = gebruikerService.createGebruiker(newGebruikerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGebruiker);
    }
}