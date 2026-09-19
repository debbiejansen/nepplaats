package nl.novi.nepplaats.service;

import nl.novi.nepplaats.dto.rol.RolDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.Rol;
import nl.novi.nepplaats.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Haal alle rol op en zet ze om naar DTO's
    public List<RolDto> getAllRols() {
        List<Rol> rols = rolRepository.findAll();
        List<RolDto> rolDtos = new ArrayList<>();

        for (Rol rol : rols) {
            rolDtos.add(transferToDto(rol));
        }

        return rolDtos;
    }

    // Haal één specifieke rol op op basis van ID
    public RolDto getRolById(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Rol niet gevonden met id: " + id));
        return transferToDto(rol);
    }

    // Maak een nieuwe rol aan
    public RolDto createRol(RolDto rolDto) {
        Rol rol = transferToEntity(rolDto);
        Rol savedRol = rolRepository.save(rol);
        return transferToDto(savedRol);
    }

    // Werk een bestaande rol bij
    public RolDto updateRol(Long id, RolDto rolDto) {
        Rol existingRol = rolRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Rol niet gevonden met id: " + id));

        // Pas de gegevens aan
        existingRol.setRolNaam(rolDto.getRolNaam());
        existingRol.setRolBeschrijving(rolDto.getRolBeschrijving());

        // Sla de gewijzigde entiteit op
        Rol updatedRol = rolRepository.save(existingRol);
        return transferToDto(updatedRol);
    }

    // Verwijder een rol op basis van ID
    public void deleteRol(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RecordNotFoundException("Rol niet gevonden met id: " + id);
        }
        rolRepository.deleteById(id);
    }

    // Helper methode: Entiteit -> DTO
    private RolDto transferToDto(Rol rol) {
        RolDto dto = new RolDto();
        dto.setRolId(rol.getRolId());
        dto.setRolNaam(rol.getRolNaam());
        dto.setRolBeschrijving(rol.getRolBeschrijving());
        return dto;
    }

    // Helper methode: DTO -> Entiteit
    private Rol transferToEntity(RolDto dto) {
        Rol rol = new Rol();
        rol.setRolNaam(dto.getRolNaam());
        rol.setRolBeschrijving(dto.getRolBeschrijving());
        return rol;
    }
}