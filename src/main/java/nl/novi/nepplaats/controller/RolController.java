package nl.novi.nepplaats.controller;

import nl.novi.nepplaats.dto.rol.RolDto;
import nl.novi.nepplaats.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/rollen")
public class RolController {

    private final RolService rolService;

    // Injecteer de Service via de constructor
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    // GET: Gegevens ophalen
    @GetMapping
    public ResponseEntity<List<RolDto>> getAllRols() {
        return ResponseEntity.ok(rolService.getAllRols());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDto> getRolById(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.getRolById(id));
    }

    // POST: Nieuwe gegevens toevoegen
    @PostMapping
    public ResponseEntity<RolDto> createRol(@RequestBody RolDto rolDto) {
        RolDto created = rolService.createRol(rolDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT: Gegevens updaten
    @PutMapping("/{id}")
    public ResponseEntity<RolDto> updateRol(@PathVariable Long id, @RequestBody RolDto rolDto) {
        return ResponseEntity.ok(rolService.updateRol(id, rolDto));
    }

    // DELETE: Gegevens verwijderen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
        rolService.deleteRol(id);
        return ResponseEntity.noContent().build();
    }
}