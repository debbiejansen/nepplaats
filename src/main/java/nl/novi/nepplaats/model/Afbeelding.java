package nl.novi.nepplaats.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "afbeelding")
public class Afbeelding {

    @Id
    @Column(name = "afbeelding_id", nullable = false, updatable = false)
    private UUID afbeeldingId;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "bestand_data", nullable = false)
    private byte[] bestandData;

    @Column(name = "originele_naam", nullable = false)
    private String origineleNaam;

    @Column(name = "bestandstype", nullable = false)
    private String bestandstype;

    @Column(name = "bestandsgrootte", nullable = false)
    private Long bestandsgrootte;

    @CreationTimestamp
    @Column(name = "upload_datum", updatable = false)
    private LocalDateTime uploadDatum;

    public Afbeelding() {}

    public Afbeelding(UUID afbeeldingId, byte[] bestandData, String origineleNaam, String bestandstype, Long bestandsgrootte) {
        this.afbeeldingId = afbeeldingId;
        this.bestandData = bestandData;
        this.origineleNaam = origineleNaam;
        this.bestandstype = bestandstype;
        this.bestandsgrootte = bestandsgrootte;
    }

    // Getters & Setters
    public UUID getAfbeeldingId() { return afbeeldingId; }
    public void setAfbeeldingId(UUID afbeeldingId) { this.afbeeldingId = afbeeldingId; }

    public byte[] getBestandData() { return bestandData; }
    public void setBestandData(byte[] bestandData) { this.bestandData = bestandData; }

    public String getOrigineleNaam() { return origineleNaam; }
    public void setOrigineleNaam(String origineleNaam) { this.origineleNaam = origineleNaam; }

    public String getBestandstype() { return bestandstype; }
    public void setBestandstype(String bestandstype) { this.bestandstype = bestandstype; }

    public Long getBestandsgrootte() { return bestandsgrootte; }
    public void setBestandsgrootte(Long bestandsgrootte) { this.bestandsgrootte = bestandsgrootte; }

    public LocalDateTime getUploadDatum() { return uploadDatum; }
    public void setUploadDatum(LocalDateTime uploadDatum) { this.uploadDatum = uploadDatum; }
}