package nl.novi.nepplaats.repository;

import nl.novi.nepplaats.model.Afbeelding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AfbeeldingRepository extends JpaRepository<Afbeelding, UUID> {
}