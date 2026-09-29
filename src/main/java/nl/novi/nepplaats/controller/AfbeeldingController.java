package nl.novi.nepplaats.controller;

import nl.novi.nepplaats.model.Afbeelding;
import nl.novi.nepplaats.service.AfbeeldingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/afbeeldingen")
public class AfbeeldingController {

    private final AfbeeldingService service;

    public AfbeeldingController(AfbeeldingService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<Afbeelding> uploadAfbeelding(@RequestParam("file") MultipartFile file) throws IOException {
        Afbeelding upload = service.uploadBestand(file);
        return ResponseEntity.ok(upload);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadAfbeelding(@PathVariable UUID id) {
        Afbeelding afbeelding = service.getAfbeelding(id);
        // Fallback voor MediaType als het bestandstype niet goed geparsed kan worden
        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(afbeelding.getBestandstype());
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + afbeelding.getOrigineleNaam() + "\"")
                .body(afbeelding.getBestandData());
    }
}