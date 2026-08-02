package nl.novi.nepplaats.dto.register;

public class RegisterRequest {
    private String gebruikersnaam;
    private String email;
    private String wachtwoord;
    private String beschrijving;

    // Getters en Setters
    public String getGebruikersnaam() { return gebruikersnaam; }
    public void setGebruikersnaam(String gebruikersnaam) { this.gebruikersnaam = gebruikersnaam; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWachtwoord() { return wachtwoord; }
    public void setWachtwoord(String wachtwoord) { this.wachtwoord = wachtwoord; }

    public String getBeschrijving() { return beschrijving; }
    public void setBeschrijving(String beschrijving) { this.beschrijving = beschrijving; }
}