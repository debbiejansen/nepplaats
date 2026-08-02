package nl.novi.nepplaats.model;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_posts")
public class ProductPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_post_id")
    private Long productPostId;

    @Column(name = "titel", nullable = false)
    private String titel;

    @Column(name = "beschrijving", columnDefinition = "TEXT")
    private String beschrijving;

    @Column(name = "prijs", nullable = false)
    private BigDecimal prijs;

    @Column(name = "afbeelding")
    private String afbeelding;

    @CreationTimestamp
    @Column(name = "post_date", updatable = false)
    private LocalDateTime postDate;

    @Column(name = "poster_id", nullable = false)
    private Long posterId;

    @Column(name = "categorie_id")
    private Long categorieId;

    @Column(name = "status_id")
    private Long statusId;

    // Standaard constructors
    public ProductPost() {
    }

    public ProductPost(String titel, BigDecimal prijs, Long posterId) {
        this.titel = titel;
        this.prijs = prijs;
        this.posterId = posterId;
    }

    // Getters en Setters
    public Long getProductPostId() {
        return productPostId;
    }

    public void setProductPostId(Long productPostId) {
        this.productPostId = productPostId;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public BigDecimal getPrijs() {
        return prijs;
    }

    public void setPrijs(BigDecimal prijs) {
        this.prijs = prijs;
    }

    public String getAfbeelding() {
        return afbeelding;
    }

    public void setAfbeelding(String afbeelding) {
        this.afbeelding = afbeelding;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public Long getPosterId() {
        return posterId;
    }

    public void setPosterId(Long posterId) {
        this.posterId = posterId;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
}