package nl.novi.nepplaats.controller;

import jakarta.validation.Valid;
import nl.novi.nepplaats.dto.gebruiker.GebruikerDto;
import nl.novi.nepplaats.service.GebruikerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/gebruikers")
public class GebruikerController {

    private final GebruikerService gebruikerService;

    // Injecteer de Service via de constructor
    public GebruikerController(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    // GET: ophalen gegevens
    @GetMapping
    public ResponseEntity<List<GebruikerDto.Response>> getAllGebruikers() {
        return ResponseEntity.ok(gebruikerService.getAllGebruikers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GebruikerDto.Response> getGebruikerById(@PathVariable Long id) {
        return ResponseEntity.ok(gebruikerService.getGebruikerById(id));
    }

    // POST: Nieuwe gebruiker toevoegen
    @PostMapping
    public ResponseEntity<GebruikerDto.Response> createGebruiker(@Valid @RequestBody GebruikerDto.Request gebruikerDto) {
        GebruikerDto.Response created = gebruikerService.createGebruiker(gebruikerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT: Gegevens wijzigen
    @PutMapping("/{id}")
    public ResponseEntity<GebruikerDto.Response> updateGebruiker(@PathVariable Long id, @Valid @RequestBody GebruikerDto.Request gebruikerDto) {
        return ResponseEntity.ok(gebruikerService.updateGebruiker(id, gebruikerDto));
    }

    // DELETE: Gegevens verwijderen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGebruiker(@PathVariable Long id) {
        gebruikerService.deleteGebruiker(id);
        return ResponseEntity.noContent().build();
    }
}