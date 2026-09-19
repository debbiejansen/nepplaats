package nl.novi.nepplaats.controller;

import jakarta.validation.Valid;
import nl.novi.nepplaats.dto.transactie.TransactieDto;
import nl.novi.nepplaats.service.TransactieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacties")
public class TransactieController {

    private final TransactieService transactieService;

    // Injecteer de Service via de constructor
    public TransactieController(TransactieService transactieService) {
        this.transactieService = transactieService;
    }

    // GET: Alle transacties ophalen
    @GetMapping
    public ResponseEntity<List<TransactieDto.Response>> getAllTransacties() {
        return ResponseEntity.ok(transactieService.getAllTransacties());
    }

    // GET: Één transactie ophalen op basis van ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactieDto.Response> getTransactieById(@PathVariable Long id) {
        return ResponseEntity.ok(transactieService.getTransactieById(id));
    }

    // POST: Nieuwe transactie aanmaken
    @PostMapping
    public ResponseEntity<TransactieDto.Response> createTransactie(@Valid @RequestBody TransactieDto.Request transactieDto) {
        TransactieDto.Response created = transactieService.createTransactie(transactieDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // DELETE: Transactie verwijderen op basis van ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactie(@PathVariable Long id) {
        transactieService.deleteTransactie(id);
        return ResponseEntity.noContent().build();
    }
}