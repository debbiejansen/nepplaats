package nl.novi.nepplaats.repository;

import nl.novi.nepplaats.model.Gebruiker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GebruikerRepository extends JpaRepository<Gebruiker, Long> {

    // Zoekt een gebruiker op basis van unieke gebruikersnaam
    Optional<Gebruiker> findByGebruikersnaam(String gebruikersnaam);

    // Zoekt een gebruiker op basis van uniek e-mailadres
    Optional<Gebruiker> findByEmail(String email);

    // Controleert of een e-mailadres al in gebruik is
    boolean existsByEmail(String email);

    // Vindt alle gebruikers met een specifieke rolID
    List<Gebruiker> findByRolId(Long rolId);

    // Voorbeeld van een JPQL query: Vindt gebruikers op basis van zoekterm in gebruikersnaam
    @Query("SELECT g FROM Gebruiker g WHERE g.gebruikersnaam LIKE %:zoekterm%")
    List<Gebruiker> zoekGebruikersMetNaam(@Param("zoekterm") String zoekterm);
}