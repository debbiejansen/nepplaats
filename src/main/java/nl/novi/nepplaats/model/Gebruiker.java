package nl.novi.nepplaats.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gebruikers")
public class Gebruiker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long gebruikerId;

    @Column(name = "gebruikersnaam", unique = true, nullable = false)
    private String gebruikersnaam;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "wachtwoord", nullable = false)
    private String wachtwoord;

    @Column(name = "rol_id")
    private Long rolId;

    @Column(name = "beschrijving", columnDefinition = "TEXT")
    private String beschrijving;

    // Standaard constructors
    public Gebruiker() {
    }

    public Gebruiker(String gebruikersnaam, String email, String wachtwoord) {
        this.gebruikersnaam = gebruikersnaam;
        this.email = email;
        this.wachtwoord = wachtwoord;
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

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }
}