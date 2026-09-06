package nl.novi.nepplaats.repository;

import nl.novi.nepplaats.model.ProductPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

//@Repository
//public interface ProductPostRepository extends JpaRepository<ProductPost, Long> {
//
//    // Vindt alle posts van een specifieke poster/gebruiker
//    List<ProductPost> findByPosterId(Long posterId);
//
//    // Vindt alle posts binnen een specifieke categorie
//    List<ProductPost> findByCategorieId(Long categorieId);
//
//    // Vindt alle posts met een specifieke status
//    List<ProductPost> findByStatusId(Long statusId);
//
//    // Zoekt naar producten met een prijs lager dan of gelijk aan de opgegeven prijs
//    List<ProductPost> findByPrijsLessThanEqual(BigDecimal maxPrijs);
//}


// Pas GebruikerId, CategorieId of StatusId in de servicemethodes eventueel aan naar de exacte naam van de primary key
// van die specifieke entities, bijv. findByPosterId als de ID-property in Gebruiker gewoon id heet

@Repository
public interface ProductPostRepository extends JpaRepository<ProductPost, Long> {

    // JPA snapt dat hij moet zoeken op de ID van de gekoppelde Gebruiker Entity
    List<ProductPost> findByPosterGebruikerId(Long gebruikerId);

    // Zoek op ID van de Categorie Entity
    List<ProductPost> findByCategorieCategorieId(Long categorieId);

    // Zoek op ID van de Status Entity
    List<ProductPost> findByStatusStatusId(Long statusId);

    List<ProductPost> findByPrijsLessThanEqual(BigDecimal maxPrijs);
}