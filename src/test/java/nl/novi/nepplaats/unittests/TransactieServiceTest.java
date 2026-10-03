package nl.novi.nepplaats.unittests;

import nl.novi.nepplaats.dto.transactie.TransactieDto;
import nl.novi.nepplaats.model.Gebruiker;
import nl.novi.nepplaats.model.ProductPost;
import nl.novi.nepplaats.model.Status;
import nl.novi.nepplaats.model.Transactie;
import nl.novi.nepplaats.repository.GebruikerRepository;
import nl.novi.nepplaats.repository.ProductPostRepository;
import nl.novi.nepplaats.repository.StatusRepository;
import nl.novi.nepplaats.repository.TransactieRepository;
import nl.novi.nepplaats.service.TransactieService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactieServiceTest {

    @Mock
    private TransactieRepository transactieRepository;

    @Mock
    private ProductPostRepository productPostRepository;

    @Mock
    private GebruikerRepository gebruikerRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private TransactieService transactieService;

    private Gebruiker verkoper;
    private Gebruiker koper;
    private ProductPost product;
    private Status statusBeschikbaar;
    private Status statusVerkocht;

    @BeforeEach
    void setUp() {
        statusBeschikbaar = new Status();
        statusBeschikbaar.setStatusId(1L);

        statusVerkocht = new Status();
        statusVerkocht.setStatusId(3L);

        // Verkoper instellen
        verkoper = new Gebruiker();
        verkoper.setGebruikerId(1L);
        verkoper.setKeycloakId("keycloak-verkoper-111");

        // Koper instellen
        koper = new Gebruiker();
        koper.setGebruikerId(2L);
        koper.setKeycloakId("keycloak-koper-222");

        // Product inschakelen met 'verkoper' als poster
        product = new ProductPost();
        product.setProductPostId(50L);
        product.setPoster(verkoper); // Verkoper zit hier in opgeborgen!
        product.setStatus(statusBeschikbaar);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void mockSecurityContext(String keycloakId, List<String> roles) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(keycloakId);
        when(jwt.getClaimAsStringList("roles")).thenReturn(roles);
    }

    @Test
    @DisplayName("6. Kopen: Maakt transactie aan en zet productstatus direct op Verkocht (3)")
    void createTransactie_Succesvol_MaaktTransactieAanEnVerandertStatusNaarVerkocht() {
        // Arrange
        TransactieDto.Request request = new TransactieDto.Request();
        request.setProductPostId(50L);
        request.setKoperId(2L);

        when(productPostRepository.findById(50L)).thenReturn(Optional.of(product));
        when(gebruikerRepository.findById(2L)).thenReturn(Optional.of(koper));
        when(statusRepository.findById(3L)).thenReturn(Optional.of(statusVerkocht));
        when(transactieRepository.save(any(Transactie.class))).thenAnswer(i -> {
            Transactie t = i.getArgument(0);
            t.setTransactieId(99L);
            return t;
        });

        // Act
        TransactieDto.Response response = transactieService.createTransactie(request);

        // Assert
        assertNotNull(response);
        assertEquals(3L, product.getStatus().getStatusId(), "Product status moet 3 (Verkocht) worden");
        verify(productPostRepository, times(1)).save(product);
        verify(transactieRepository, times(1)).save(any(Transactie.class));
    }

    @Test
    @DisplayName("7. Business Rule: Verkoper mag zijn eigen product niet kopen")
    void createTransactie_VerkoperKooptEigenProduct_GooitIllegalArgumentException() {
        // Arrange
        TransactieDto.Request request = new TransactieDto.Request();
        request.setProductPostId(50L);
        request.setKoperId(1L); // 1L is het ID van de verkoper (poster)

        when(productPostRepository.findById(50L)).thenReturn(Optional.of(product));
        when(gebruikerRepository.findById(1L)).thenReturn(Optional.of(verkoper));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            transactieService.createTransactie(request);
        });

        assertEquals("Je kunt je eigen product niet kopen.", exception.getMessage());
        verify(transactieRepository, never()).save(any());
    }

    @Test
    @DisplayName("8. Business Rule: Reeds verkocht product kan niet nogmaals gekocht worden")
    void createTransactie_ProductAlVerkocht_GooitIllegalStateException() {
        // Arrange
        product.setStatus(statusVerkocht);

        TransactieDto.Request request = new TransactieDto.Request();
        request.setProductPostId(50L);
        request.setKoperId(2L);

        when(productPostRepository.findById(50L)).thenReturn(Optional.of(product));
        when(gebruikerRepository.findById(2L)).thenReturn(Optional.of(koper));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            transactieService.createTransactie(request);
        });

        assertTrue(exception.getMessage().contains("ProductPost is niet beschikbaar of gereserveerd."));
        verify(transactieRepository, never()).save(any());
    }

    @Test
    @DisplayName("9. Privacy: Gewone gebruiker haalt alleen eigen transacties op")
    void getTransacties_GewoneGebruiker_ZietAlleenEigenTransacties() {
        // Arrange
        mockSecurityContext("keycloak-koper-222", List.of("USER"));

        // Gebruik enkel de schone constructor van Transactie
        Transactie t1 = new Transactie(product, koper);
        t1.setTransactieId(1L);

        when(transactieRepository.findByKoper_KeycloakIdOrProductPost_Poster_KeycloakId("keycloak-koper-222", "keycloak-koper-222"))
                .thenReturn(List.of(t1));

        // Act
        List<TransactieDto.Response> resultaat = transactieService.getTransactiesVoorGebruiker();

        // Assert
        assertEquals(1, resultaat.size());
        verify(transactieRepository, times(1))
                .findByKoper_KeycloakIdOrProductPost_Poster_KeycloakId("keycloak-koper-222", "keycloak-koper-222");
        verify(transactieRepository, never()).findAll();
    }

    @Test
    @DisplayName("10. Beheer: Admin haalt ALLE transacties in het systeem op")
    void getTransacties_Admin_ZietAlleTransacties() {
        // Arrange
        mockSecurityContext("keycloak-admin-999", List.of("ADMIN"));

        Transactie t1 = new Transactie(product, koper);
        t1.setTransactieId(1L);

        when(transactieRepository.findAll()).thenReturn(List.of(t1));

        // Act
        List<TransactieDto.Response> resultaat = transactieService.getTransactiesVoorGebruiker();

        // Assert
        assertEquals(1, resultaat.size());
        verify(transactieRepository, times(1)).findAll();
        verify(transactieRepository, never()).findByKoper_KeycloakIdOrProductPost_Poster_KeycloakId(any(), any());
    }
}