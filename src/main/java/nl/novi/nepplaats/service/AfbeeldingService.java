package nl.novi.nepplaats.service;

import nl.novi.nepplaats.model.Afbeelding;
import nl.novi.nepplaats.repository.AfbeeldingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AfbeeldingService {

    private final AfbeeldingRepository repository;

    public AfbeeldingService(AfbeeldingRepository repository) {
        this.repository = repository;
    }

    public Afbeelding uploadBestand(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Kan geen leeg bestand opslaan");
        }

        String origineleNaam = file.getOriginalFilename();

        Afbeelding afbeelding = new Afbeelding(
                file.getBytes(),
                origineleNaam != null ? origineleNaam : "onbekend",
                file.getContentType() != null ? file.getContentType() : "application/octet-stream",
                file.getSize()
        );

        return repository.save(afbeelding);
    }

    @Transactional(readOnly = true)
    public Afbeelding getAfbeelding(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Afbeelding niet gevonden met id: " + id));
    }
}