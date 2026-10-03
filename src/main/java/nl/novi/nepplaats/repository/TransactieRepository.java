package nl.novi.nepplaats.repository;

import nl.novi.nepplaats.model.Transactie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactieRepository extends JpaRepository<Transactie, Long> {

    List<Transactie> findByKoper_GebruikerId(Long koperId);

    Optional<Transactie> findByProductPost_ProductPostId(Long productPostId);

    boolean existsByProductPost_ProductPostId(Long productPostId);

    List<Transactie> findByKoper_KeycloakIdOrProductPost_Poster_KeycloakId(String koperKeycloakId, String verkoperKeycloakId);
}