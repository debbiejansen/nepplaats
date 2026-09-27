package nl.novi.nepplaats.dto.gebruiker;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GebruikerDto {

    public static class Request {

        @NotBlank(message = "Gebruikersnaam is verplicht")
        @Size(min = 2, max = 30, message = "Gebruikersnaam moet tussen 2 en 30 tekens lang zijn")
        private String gebruikersnaam;

        @NotBlank(message = "E-mailadres is verplicht")
        @Email(message = "Voer een geldig e-mailadres in")
        private String email;

        @NotNull(message = "Rol ID is verplicht")
        private Long rolId;

        private String beschrijving;

        // Constructors
        public Request() {
        }

        public Request(String gebruikersnaam, String email, Long rolId, String beschrijving) {
            this.gebruikersnaam = gebruikersnaam;
            this.email = email;
            this.rolId = rolId;
            this.beschrijving = beschrijving;
        }

        // Getters en Setters
        public String getGebruikersnaam() { return gebruikersnaam; }
        public void setGebruikersnaam(String gebruikersnaam) { this.gebruikersnaam = gebruikersnaam; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Long getRolId() { return rolId; }
        public void setRolId(Long rolId) { this.rolId = rolId; }

        public String getBeschrijving() { return beschrijving; }
        public void setBeschrijving(String beschrijving) { this.beschrijving = beschrijving; }
    }

    // Response DTO (Voor GET / POST)
    public static class Response {

        private Long gebruikerId;
        private String gebruikersnaam;
        private String email;
        private Long rolId;
        private String beschrijving;

        // Constructors
        public Response() {
        }

        public Response(Long gebruikerId, String gebruikersnaam, String email, Long rolId, String beschrijving) {
            this.gebruikerId = gebruikerId;
            this.gebruikersnaam = gebruikersnaam;
            this.email = email;
            this.rolId = rolId;
            this.beschrijving = beschrijving;
        }

        // Getters en Setters
        public Long getGebruikerId() { return gebruikerId; }
        public void setGebruikerId(Long gebruikerId) { this.gebruikerId = gebruikerId; }

        public String getGebruikersnaam() { return gebruikersnaam; }
        public void setGebruikersnaam(String gebruikersnaam) { this.gebruikersnaam = gebruikersnaam; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Long getRolId() { return rolId; }
        public void setRolId(Long rolId) { this.rolId = rolId; }

        public String getBeschrijving() { return beschrijving; }
        public void setBeschrijving(String beschrijving) { this.beschrijving = beschrijving; }
    }
}