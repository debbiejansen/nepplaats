package nl.novi.nepplaats.repository;

import nl.novi.nepplaats.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    // Vindt een categorie op basis van unieke naam
    Optional<Categorie> findByCategorieNaam(String categorieNaam);

    // Controleert of een categorie al bestaat
    boolean existsByCategorieNaam(String categorieNaam);
}