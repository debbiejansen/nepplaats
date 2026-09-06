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

    @GetMapping
    public ResponseEntity<List<StatusDto>> getAllStatussen() {
        List<StatusDto> dtos = statusService.getAllStatussen();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusDto> getStatusById(@PathVariable Long id) {
        StatusDto dto = statusService.getStatusById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<StatusDto> createStatus(@RequestBody StatusDto statusDto) {
        StatusDto createdStatus = statusService.createStatus(statusDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
    }

}