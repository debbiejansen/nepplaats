package nl.novi.nepplaats.dto.gebruiker;

public class GebruikerDto {

    private Long gebruikerId;
    private String gebruikersnaam;
    private String email;
    private String wachtwoord;
    private Long rolId;
    private String beschrijving;

    public GebruikerDto() {
    }
    public GebruikerDto(String gebruikersnaam, String email, String wachtwoord, Long rolId, String beschrijving) {
        this.gebruikersnaam = gebruikersnaam;
        this.email = email;
        this.wachtwoord = wachtwoord;
        this.rolId = rolId;
        this.beschrijving = beschrijving;
    }

    // Constructor
    public GebruikerDto(Long gebruikerId, String gebruikersnaam, String email, String wachtwoord, Long rolId, String beschrijving) {
        this.gebruikerId = gebruikerId;
        this.gebruikersnaam = gebruikersnaam;
        this.email = email;
        this.wachtwoord = wachtwoord;
        this.rolId = rolId;
        this.beschrijving = beschrijving;
    }

    // Getters + Setters
    public Long getGebruikerId() { return gebruikerId; }
    public void setGebruikerId(Long gebruikerId) { this.gebruikerId = gebruikerId; }

    public String getGebruikersnaam() { return gebruikersnaam; }
    public void setGebruikersnaam(String gebruikersnaam) { this.gebruikersnaam = gebruikersnaam; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWachtwoord() { return wachtwoord; }
    public void setWachtwoord(String wachtwoord) { this.wachtwoord = wachtwoord; }

    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }

    public String getBeschrijving() { return beschrijving; }
    public void setBeschrijving(String beschrijving) { this.beschrijving = beschrijving; }

}