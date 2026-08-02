package nl.novi.nepplaats.dto.login;

public class LoginRequest {
    private String email;
    private String wachtwoord;

    // Getters en Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getWachtwoord() { return wachtwoord; }
    public void setWachtwoord(String wachtwoord) { this.wachtwoord = wachtwoord; }
}