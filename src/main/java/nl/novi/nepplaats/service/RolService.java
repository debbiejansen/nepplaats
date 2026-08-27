package nl.novi.nepplaats.service;

import nl.novi.nepplaats.dto.rol.RolDto;
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

    // Maak een nieuwe rol aan
    public RolDto createRol(RolDto rolDto) {
        // Zet DTO om naar Entiteit
        Rol rol = transferToEntity(rolDto);

        // Sla de entiteit op in de database via de repository
        Rol savedRol = rolRepository.save(rol);

        // Zet het opgeslagen resultaat weer om naar een DTO om terug te sturen
        return transferToDto(savedRol);
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