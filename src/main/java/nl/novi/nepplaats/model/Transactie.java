package nl.novi.nepplaats.model;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacties")
public class Transactie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactie_id")
    private Long transactieId;

    @Column(name = "original_product_post_id", nullable = false)
    private Long originalProductPostId;

    @Column(name = "koper_id", nullable = false)
    private Long koperId;

    @CreationTimestamp
    @Column(name = "tijd", updatable = false)
    private LocalDateTime tijd;

    // Standaard constructors
    public Transactie() {
    }

    public Transactie(Long originalProductPostId, Long koperId) {
        this.originalProductPostId = originalProductPostId;
        this.koperId = koperId;
    }

    // Getters en Setters
    public Long getTransactieId() {
        return transactieId;
    }

    public void setTransactieId(Long transactieId) {
        this.transactieId = transactieId;
    }

    public Long getOriginalProductPostId() {
        return originalProductPostId;
    }

    public void setOriginalProductPostId(Long originalProductPostId) {
        this.originalProductPostId = originalProductPostId;
    }

    public Long getKoperId() {
        return koperId;
    }

    public void setKoperId(Long koperId) {
        this.koperId = koperId;
    }

    public LocalDateTime getTijd() {
        return tijd;
    }

    public void setTijd(LocalDateTime tijd) {
        this.tijd = tijd;
    }
}