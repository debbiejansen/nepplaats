package nl.novi.nepplaats.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.novi.nepplaats.service.GebruikerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserSynchronizationFilter extends OncePerRequestFilter {

    private final GebruikerService gebruikerService;

    public UserSynchronizationFilter(GebruikerService gebruikerService) {
        this.gebruikerService = gebruikerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            // Zodra er een geldig JWT is, zorgen we dat de gebruiker in onze DB staat
            gebruikerService.getOrCreateGebruikerFromJwt(jwt);
        }

        filterChain.doFilter(request, response);
    }
}