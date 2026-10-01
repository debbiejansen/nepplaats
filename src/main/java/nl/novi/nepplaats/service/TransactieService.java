package nl.novi.nepplaats.service;

import nl.novi.nepplaats.dto.transactie.TransactieDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.Gebruiker;
import nl.novi.nepplaats.model.ProductPost;
import nl.novi.nepplaats.model.Status;
import nl.novi.nepplaats.model.Transactie;
import nl.novi.nepplaats.repository.GebruikerRepository;
import nl.novi.nepplaats.repository.ProductPostRepository;
import nl.novi.nepplaats.repository.StatusRepository;
import nl.novi.nepplaats.repository.TransactieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactieService {

    private final TransactieRepository transactieRepository;
    private final ProductPostRepository productPostRepository;
    private final GebruikerRepository gebruikerRepository;
    private final StatusRepository statusRepository;
    public TransactieService(TransactieRepository transactieRepository,
                             ProductPostRepository productPostRepository,
                             GebruikerRepository gebruikerRepository,
                             StatusRepository statusRepository) {
        this.transactieRepository = transactieRepository;
        this.productPostRepository = productPostRepository;
        this.gebruikerRepository = gebruikerRepository;
        this.statusRepository = statusRepository;
    }

    // Haal alle transacties op en zet ze om naar DTO's
    public List<TransactieDto.Response> getAllTransacties() {
        List<Transactie> transacties = transactieRepository.findAll();
        List<TransactieDto.Response> dtos = new ArrayList<>();

        for (Transactie transactie : transacties) {
            dtos.add(toResponseDto(transactie));
        }

        return dtos;
    }

    // Haal één transactie op basis van ID
    public TransactieDto.Response getTransactieById(Long id) {
        Transactie transactie = transactieRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Transactie niet gevonden met id: " + id));

        return toResponseDto(transactie);
    }

    // AUTOMATISCHE STATUSOVERGANG NAAR 3 (Verkocht) BIJ CREATIE TRANSACTIE
    @Transactional
    public TransactieDto.Response createTransactie(TransactieDto.Request dto) {
        if (transactieRepository.existsByProductPost_ProductPostId(dto.getProductPostId())) {
            throw new IllegalStateException("Er bestaat al een transactie voor ProductPost id: " + dto.getProductPostId());
        }

        ProductPost productPost = productPostRepository.findById(dto.getProductPostId())
                .orElseThrow(() -> new RecordNotFoundException("ProductPost niet gevonden met id: " + dto.getProductPostId()));

        Gebruiker koper = gebruikerRepository.findById(dto.getKoperId())
                .orElseThrow(() -> new RecordNotFoundException("Koper (Gebruiker) niet gevonden met id: " + dto.getKoperId()));

        // CHECK: Eigenaar van het product mag het product niet zelf kopen
        if (productPost.getPoster() != null && productPost.getPoster().getGebruikerId().equals(koper.getGebruikerId())) {
            throw new IllegalStateException("Je kunt je eigen product niet kopen.");
        }

        // Valideer of de status 1 (Beschikbaar) of 2 (Gereserveerd) is
        Long huidigeStatusId = (productPost.getStatus() != null) ? productPost.getStatus().getStatusId() : null;
        if (huidigeStatusId == null || (huidigeStatusId != 1L && huidigeStatusId != 2L)) {
            throw new IllegalStateException("Transactie kan niet worden aangemaakt: ProductPost is niet beschikbaar of gereserveerd.");
        }

        // Pas status van ProductPost aan naar 3 (Verkocht)
        Status verkochtStatus = statusRepository.findById(3L)
                .orElseThrow(() -> new RecordNotFoundException("Status 'Verkocht' (id: 3) niet gevonden in database."));
        productPost.setStatus(verkochtStatus);
        productPostRepository.save(productPost);

        // Maak transactie aan
        Transactie transactie = new Transactie();
        transactie.setProductPost(productPost);
        transactie.setKoper(koper);

        Transactie savedTransactie = transactieRepository.save(transactie);

        return toResponseDto(savedTransactie);
    }
    // Verwijder een transactie op basis van ID
    public void deleteTransactie(Long id) {
        if (!transactieRepository.existsById(id)) {
            throw new RecordNotFoundException("Transactie niet gevonden met id: " + id);
        }
        transactieRepository.deleteById(id);
    }

    // Helper methode: DTO -> Entiteit
    private Transactie toEntity(TransactieDto.Request dto) {
        Transactie transactie = new Transactie();

        ProductPost productPost = productPostRepository.findById(dto.getProductPostId())
                .orElseThrow(() -> new RecordNotFoundException("ProductPost niet gevonden met id: " + dto.getProductPostId()));
        transactie.setProductPost(productPost);

        Gebruiker koper = gebruikerRepository.findById(dto.getKoperId())
                .orElseThrow(() -> new RecordNotFoundException("Koper niet gevonden met id: " + dto.getKoperId()));
        transactie.setKoper(koper);
        return transactie;
    }

    // Helper methode: Entiteit -> DTO
    private TransactieDto.Response toResponseDto(Transactie entity) {
        TransactieDto.Response dto = new TransactieDto.Response();
        dto.setTransactieId(entity.getTransactieId());
        if (entity.getKoper() != null) {
            dto.setKoperId(entity.getKoper().getGebruikersnaam() != null ? entity.getKoper().getGebruikerId() : null);
        }
        if (entity.getProductPost() != null) {
            dto.setProductPostId(entity.getProductPost().getProductPostId());
        }
        dto.setTijd(entity.getTijd());
        return dto;
    }
}