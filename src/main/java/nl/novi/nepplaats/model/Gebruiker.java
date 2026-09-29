package nl.novi.nepplaats.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gebruikers")
public class Gebruiker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long gebruikerId;

    @Column(name = "gebruikersnaam")
    private String gebruikersnaam;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "keycloak_id", nullable = false)
    private String keycloakId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(name = "beschrijving", columnDefinition = "TEXT")
    private String beschrijving;

    // Standaard constructors
    public Gebruiker() {
    }

    public Gebruiker(String gebruikersnaam, String email, String keycloakId) {
        this.gebruikersnaam = gebruikersnaam;
        this.email = email;
        this.keycloakId = keycloakId;
    }

    // Getters en Setters
    public Long getGebruikerId() {
        return gebruikerId;
    }

    public void setGebruikerId(Long gebruikerId) {
        this.gebruikerId = gebruikerId;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }
}