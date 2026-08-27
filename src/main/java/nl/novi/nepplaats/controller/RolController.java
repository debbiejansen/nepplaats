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

    // POST: Nieuwe categorie toevoegen
    @PostMapping("/create")
    public ResponseEntity<RolDto> createRol(@RequestBody RolDto newRolDto) {
        RolDto createdRol = rolService.createRol(newRolDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRol);
    }
}