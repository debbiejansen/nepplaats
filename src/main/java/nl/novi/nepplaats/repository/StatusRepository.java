package nl.novi.nepplaats.repository;

import nl.novi.nepplaats.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    // Vindt een status op basis van unieke naam
    Optional<Status> findByStatusNaam(String statusNaam);

    // Controleert of een statusnaam al bestaat
    boolean existsByStatusNaam(String statusNaam);
}