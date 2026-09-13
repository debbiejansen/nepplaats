package nl.novi.nepplaats.controller;

import nl.novi.nepplaats.dto.status.StatusDto;
import nl.novi.nepplaats.service.StatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/statussen")
public class StatusController {

    private final StatusService statusService;

    // Injecteer de Service via de constructor
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    // GET: Gegevens ophalen
    @GetMapping
    public ResponseEntity<List<StatusDto>> getAllStatussen() {
        return ResponseEntity.ok(statusService.getAllStatussen());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusDto> getStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(statusService.getStatusById(id));
    }

    // POST: Gegevens sturen/aanmaken
    @PostMapping
    public ResponseEntity<StatusDto> createStatus(@RequestBody StatusDto statusDto) {
        StatusDto created = statusService.createStatus(statusDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT: Gegevens updaten
    @PutMapping("/{id}")
    public ResponseEntity<StatusDto> updateStatus(@PathVariable Long id, @RequestBody StatusDto statusDto) {
        return ResponseEntity.ok(statusService.updateStatus(id, statusDto));
    }

    // DELETE: Gegevens verwijderen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}