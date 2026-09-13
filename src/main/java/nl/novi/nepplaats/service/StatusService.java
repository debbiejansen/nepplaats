package nl.novi.nepplaats.service;

import nl.novi.nepplaats.dto.status.StatusDto;
import nl.novi.nepplaats.model.Status;
import nl.novi.nepplaats.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    // insert de Repository i.p.v. de Service
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    // CRUD
    public List<StatusDto> getAllStatussen() {
        List<Status> statussen = statusRepository.findAll();
        List<StatusDto> statusDtos = new ArrayList<>();

        for (Status status : statussen) {
            statusDtos.add(transferToDto(status));
        }
        return statusDtos;
    }

    public StatusDto getStatusById(Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status niet gevonden met id: " + id));
        return transferToDto(status);
    }

    public StatusDto createStatus(StatusDto dto) {
        Status status = transferToEntity(dto);
        Status savedStatus = statusRepository.save(status);
        return transferToDto(savedStatus);
    }

    public StatusDto updateStatus(Long id, StatusDto newStatus) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status niet gevonden met id: " + id));

        status.setStatusNaam(newStatus.getStatusNaam());
        status.setStatusBeschrijving(newStatus.getStatusBeschrijving());

        Status updatedStatus = statusRepository.save(status);
        return transferToDto(updatedStatus);
    }

    public void deleteStatus(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new RuntimeException("Status niet gevonden met id: " + id);
        }
        statusRepository.deleteById(id);
    }

    // Helper methode: DTO -> Entiteit
    private Status transferToEntity(StatusDto dto) {
        Status status = new Status();
        status.setStatusNaam(dto.getStatusNaam());
        status.setStatusBeschrijving(dto.getStatusBeschrijving());
        return status;
    }

    // Helper methode: Entiteit -> DTO
    private StatusDto transferToDto(Status status) {
        StatusDto dto = new StatusDto();
        dto.setStatusId(status.getStatusId());
        dto.setStatusNaam(status.getStatusNaam());
        dto.setStatusBeschrijving(status.getStatusBeschrijving());
        return dto;
    }
}