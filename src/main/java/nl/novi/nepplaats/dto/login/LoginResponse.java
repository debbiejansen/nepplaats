package nl.novi.nepplaats.dto.login;

public class LoginResponse {
    private String message;
    private String token;
    private UserInfo user;

    public LoginResponse(String message, String token, UserInfo user) {
        this.message = message;
        this.token = token;
        this.user = user;
    }

    // Getters en Setters
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public UserInfo getUser() { return user; }

    // Interne klasse voor de gebruikersinfo
    public static class UserInfo {
        private int userId;
        private String gebruikersnaam;
        private String email;

        public UserInfo(int userId, String gebruikersnaam, String email) {
            this.userId = userId;
            this.gebruikersnaam = gebruikersnaam;
            this.email = email;
        }

        public int getUserId() { return userId; }
        public String getGebruikersnaam() { return gebruikersnaam; }
        public String getEmail() { return email; }
    }
}