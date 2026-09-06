package nl.novi.nepplaats.dto.status;

public class StatusDto {

    private Long statusId;
    private String statusNaam;
    private String statusBeschrijving;

    public StatusDto() {
    }

    public StatusDto(String statusNaam, String statusBeschrijving) {
        this.statusNaam = statusNaam;
        this.statusBeschrijving = statusBeschrijving;
    }

    // Constructor
    public StatusDto(Long statusId, String statusNaam, String statusBeschrijving) {
        this.statusId = statusId;
        this.statusNaam = statusNaam;
        this.statusBeschrijving = statusBeschrijving;
    }


    // Getters + Setters
    public Long getStatusId() { return statusId; }
    public void setStatusId(Long statusId) { this.statusId = statusId; }

    public String getStatusNaam() { return statusNaam; }
    public void setStatusNaam(String statusNaam) { this.statusNaam = statusNaam; }

    public String getStatusBeschrijving() { return statusBeschrijving; }
    public void setStatusBeschrijving(String statusBeschrijving) { this.statusBeschrijving = statusBeschrijving; }

}