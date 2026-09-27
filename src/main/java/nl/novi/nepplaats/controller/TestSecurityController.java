package nl.novi.nepplaats.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/test")
public class TestSecurityController {

    @GetMapping("/whoami")
    public Map<String, Object> whoAmI(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return Map.of("status", "Niet ingelogd / geen geldig JWT ontvangen");
        }
        assert jwt.getExpiresAt() != null;
        return Map.of(
                "subject", Objects.requireNonNull(jwt.getSubject()),
                "claims", jwt.getClaims(),
                "expiresAt", jwt.getExpiresAt()
        );
    }
}