package nl.novi.nepplaats.controller;

import nl.novi.nepplaats.dto.login.LoginRequest;
import nl.novi.nepplaats.dto.login.LoginResponse;
import nl.novi.nepplaats.dto.register.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // NEP database voor gebruikers
    private static final List<LoginResponse.UserInfo> mockUsers = new ArrayList<>();
    private static final List<String> mockPasswords = new ArrayList<>();
    private static int userIdCounter = 2;

    // Static block voor eertse nep-gebruiker
    static {
        mockUsers.add(new LoginResponse.UserInfo(1, "Eerste gebruiker", "eerstegebruiker@gmail.com"));
        mockPasswords.add("geheim");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//     Zoeken in nep-database of de gebruiker (al) bestaat
        for (int i = 0; i < mockUsers.size(); i++) {
            LoginResponse.UserInfo user = mockUsers.get(i);
            String password = mockPasswords.get(i);

            if (user.getEmail().equalsIgnoreCase(loginRequest.getEmail()) && password.equals(loginRequest.getWachtwoord())) {
                LoginResponse response = new LoginResponse(
                        "Inloggen succesvollll",
                        "mocked-jwt-token-xyz123",
                        user
                );
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Onjuist e-mailadres of wachtwoord");
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//
//        // Simpele hardcoded check (alsof we in de database kijken)
//        if ("eerstegebruiker@gmail.com".equals(loginRequest.getEmail()) &&
//                "geheim".equals(loginRequest.getWachtwoord())) {
//
//            // Maak de nep-gebruikersinfo aan
//            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
//                    1,
//                    "Eerste Gebruiker",
//                    "eerstegebruiker@gmail.com"
//            );
//
//            // Bouw de succes-response
//            LoginResponse response = new LoginResponse(
//                    "Inloggen succesvol",
//                    "mocked-jwt-token-xyz123",
//                    userInfo
//            );
//
//            return ResponseEntity.ok(response); // Status 200 OK
//        } else {
//            // Gegevens kloppen niet
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED) // Status 401 Unauthorized
//                    .body("Onjuist e-mailadres of wachtwoord");
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // Check of het emailadres al bestaat
        for (LoginResponse.UserInfo user : mockUsers) {
            if (user.getEmail().equalsIgnoreCase(registerRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mailadres is al in gebruik");
            }
        }

        // Maak de nieuwe user aan met een uniek ID
        LoginResponse.UserInfo newContextUser = new LoginResponse.UserInfo(
                userIdCounter++,
                registerRequest.getGebruikersnaam(),
                registerRequest.getEmail()
        );

        // Voeg toe aan nep-lijst
        mockUsers.add(newContextUser);
        mockPasswords.add(registerRequest.getWachtwoord());

        return ResponseEntity.status(HttpStatus.CREATED).body("Gebruiker succesvol geregistreerd met ID: " + newContextUser.getUserId());
    }
}