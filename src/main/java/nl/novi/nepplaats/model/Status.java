package nl.novi.nepplaats.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statussen")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "status_naam", unique = true, nullable = false)
    private String statusNaam;

    @Column(name = "status_beschrijving", columnDefinition = "TEXT")
    private String statusBeschrijving;

    // Standaard constructors
    public Status() {
    }

    public Status(String statusNaam, String statusBeschrijving) {
        this.statusNaam = statusNaam;
        this.statusBeschrijving = statusBeschrijving;
    }

    // Getters en Setters
    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatusNaam() {
        return statusNaam;
    }

    public void setStatusNaam(String statusNaam) {
        this.statusNaam = statusNaam;
    }

    public String getStatusBeschrijving() {
        return statusBeschrijving;
    }

    public void setStatusBeschrijving(String statusBeschrijving) {
        this.statusBeschrijving = statusBeschrijving;
    }
}