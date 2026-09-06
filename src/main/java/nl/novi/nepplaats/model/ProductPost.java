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

    // relaties
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id", nullable = false)
    private Gebruiker poster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    // Standaard constructors
    public ProductPost() {
    }

    public ProductPost(String titel, BigDecimal prijs, Gebruiker poster) {
        this.titel = titel;
        this.prijs = prijs;
        this.poster = poster;
    }

    // Getters en Setters
    public Long getProductPostId() { return productPostId; }
    public void setProductPostId(Long productPostId) { this.productPostId = productPostId; }

    public String getTitel() { return titel; }
    public void setTitel(String titel) { this.titel = titel; }

    public String getBeschrijving() { return beschrijving; }
    public void setBeschrijving(String beschrijving) { this.beschrijving = beschrijving; }

    public BigDecimal getPrijs() { return prijs; }
    public void setPrijs(BigDecimal prijs) { this.prijs = prijs; }

    public String getAfbeelding() { return afbeelding; }
    public void setAfbeelding(String afbeelding) { this.afbeelding = afbeelding; }

    public LocalDateTime getPostDate() { return postDate; }
    public void setPostDate(LocalDateTime postDate) { this.postDate = postDate; }

    public Gebruiker getPoster() { return poster; }
    public void setPoster(Gebruiker poster) { this.poster = poster; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}