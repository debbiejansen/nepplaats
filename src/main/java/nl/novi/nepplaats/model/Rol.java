package nl.novi.nepplaats.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rollen")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long rolId;

    @Column(name = "rol_naam", unique = true, nullable = false)
    private String rolNaam;

    @Column(name = "rol_beschrijving", columnDefinition = "TEXT")
    private String rolBeschrijving;

    // Standaard constructors
    public Rol() {
    }

    public Rol(String rolNaam, String rolBeschrijving) {
        this.rolNaam = rolNaam;
        this.rolBeschrijving = rolBeschrijving;
    }

    // Getters en Setters
    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public String getRolNaam() {
        return rolNaam;
    }

    public void setRolNaam(String rolNaam) {
        this.rolNaam = rolNaam;
    }

    public String getRolBeschrijving() {
        return rolBeschrijving;
    }

    public void setRolBeschrijving(String rolBeschrijving) {
        this.rolBeschrijving = rolBeschrijving;
    }
}