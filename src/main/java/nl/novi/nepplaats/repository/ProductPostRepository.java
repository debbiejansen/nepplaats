package nl.novi.nepplaats.repository;

import nl.novi.nepplaats.model.ProductPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductPostRepository extends JpaRepository<ProductPost, Long> {

    // Vindt alle posts van een specifieke poster/gebruiker
    List<ProductPost> findByPosterId(Long posterId);

    // Vindt alle posts binnen een specifieke categorie
    List<ProductPost> findByCategorieId(Long categorieId);

    // Vindt alle posts met een specifieke status
    List<ProductPost> findByStatusId(Long statusId);

    // Zoekt naar producten met een prijs lager dan of gelijk aan de opgegeven prijs
    List<ProductPost> findByPrijsLessThanEqual(BigDecimal maxPrijs);
}