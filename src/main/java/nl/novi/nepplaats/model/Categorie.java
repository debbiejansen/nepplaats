package nl.novi.nepplaats.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categorieen")
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categorie_id")
    private Long categorieId;

    @Column(name = "categorie_naam", unique = true, nullable = false)
    private String categorieNaam;

    @Column(name = "categorie_beschrijving", columnDefinition = "TEXT")
    private String categorieBeschrijving;

    // Standaard constructors
    public Categorie() {
    }

    public Categorie(String categorieNaam, String categorieBeschrijving) {
        this.categorieNaam = categorieNaam;
        this.categorieBeschrijving = categorieBeschrijving;
    }

    // Getters en Setters
    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }

    public String getCategorieNaam() {
        return categorieNaam;
    }

    public void setCategorieNaam(String categorieNaam) {
        this.categorieNaam = categorieNaam;
    }

    public String getCategorieBeschrijving() {
        return categorieBeschrijving;
    }

    public void setCategorieBeschrijving(String categorieBeschrijving) {
        this.categorieBeschrijving = categorieBeschrijving;
    }
}