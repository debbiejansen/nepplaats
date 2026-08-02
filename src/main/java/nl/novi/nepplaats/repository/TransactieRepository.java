package nl.novi.nepplaats.repository;

import nl.novi.nepplaats.model.Transactie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactieRepository extends JpaRepository<Transactie, Long> {

    // Vindt alle transacties gedaan door een specifieke koper
    List<Transactie> findByKoperId(Long koperId);

    // Vindt transacties gerelateerd aan een specifiek product_post ID
    List<Transactie> findByOriginalProductPostId(Long originalProductPostId);
}