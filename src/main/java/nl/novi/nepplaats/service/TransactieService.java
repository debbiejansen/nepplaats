package nl.novi.nepplaats.service;

import nl.novi.nepplaats.dto.transactie.TransactieDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.Transactie;
import nl.novi.nepplaats.repository.GebruikerRepository;
import nl.novi.nepplaats.repository.ProductPostRepository;
import nl.novi.nepplaats.repository.TransactieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactieService {

    private final TransactieRepository transactieRepository;
    private final ProductPostRepository productPostRepository;
    private final GebruikerRepository gebruikerRepository;

    public TransactieService(TransactieRepository transactieRepository,
                             ProductPostRepository productPostRepository,
                             GebruikerRepository gebruikerRepository) {
        this.transactieRepository = transactieRepository;
        this.productPostRepository = productPostRepository;
        this.gebruikerRepository = gebruikerRepository;
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

    // Maak een nieuwe transactie aan
    public TransactieDto.Response createTransactie(TransactieDto.Request dto) {
        // Valideer of het product en de koper daadwerkelijk bestaan
        if (!productPostRepository.existsById(dto.getOriginalProductPostId())) {
            throw new RecordNotFoundException("ProductPost niet gevonden met id: " + dto.getOriginalProductPostId());
        }

        if (!gebruikerRepository.existsById(dto.getKoperId())) {
            throw new RecordNotFoundException("Koper (Gebruiker) niet gevonden met id: " + dto.getKoperId());
        }

        Transactie transactie = toEntity(dto);
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
        transactie.setOriginalProductPostId(dto.getOriginalProductPostId());
        transactie.setKoperId(dto.getKoperId());
        return transactie;
    }

    // Helper methode: Entiteit -> DTO
    private TransactieDto.Response toResponseDto(Transactie entity) {
        TransactieDto.Response dto = new TransactieDto.Response();
        dto.setTransactieId(entity.getTransactieId());
        dto.setOriginalProductPostId(entity.getOriginalProductPostId());
        dto.setKoperId(entity.getKoperId());
        dto.setTijd(entity.getTijd());
        return dto;
    }
}