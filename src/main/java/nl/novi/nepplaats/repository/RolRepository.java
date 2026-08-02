package nl.novi.nepplaats.repository;

import nl.novi.nepplaats.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    // Vindt een rol op basis van unieke naam
    Optional<Rol> findByRolNaam(String rolNaam);

    // Controleert of een rol met een specifieke naam al bestaat
    boolean existsByRolNaam(String rolNaam);
}