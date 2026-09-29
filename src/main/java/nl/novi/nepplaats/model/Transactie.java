package nl.novi.nepplaats.model;

import nl.novi.nepplaats.dto.productpost.ProductPostDto;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "koper_id", nullable = false)
    private Gebruiker koper;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_post_id", nullable = false, unique = true)
    private ProductPost productPost;

    @CreationTimestamp
    @Column(name = "tijd", updatable = false)
    private LocalDateTime tijd;

    // Standaard constructors
    public Transactie() {
    }

    public Transactie(ProductPost productPost, Gebruiker koper) {
        this.productPost = productPost;
        this.koper = koper;
    }

    // Getters en Setters
    public Long getTransactieId() {
        return transactieId;
    }

    public void setTransactieId(Long transactieId) {
        this.transactieId = transactieId;
    }

    public Gebruiker getKoper() {
        return koper;
    }

    public void setKoper(Gebruiker koper) {
        this.koper = koper;
    }

    public ProductPost getProductPost() {
        return productPost;
    }

    public void setProductPost(ProductPost productPost) {
        this.productPost = productPost;
    }

    public LocalDateTime getTijd() {
        return tijd;
    }

    public void setTijd(LocalDateTime tijd) {
        this.tijd = tijd;
    }
}