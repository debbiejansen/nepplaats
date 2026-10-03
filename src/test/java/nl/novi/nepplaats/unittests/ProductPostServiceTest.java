package nl.novi.nepplaats.unittests;

import nl.novi.nepplaats.dto.productpost.ProductPostDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.Gebruiker;
import nl.novi.nepplaats.model.ProductPost;
import nl.novi.nepplaats.model.Rol;
import nl.novi.nepplaats.model.Status;
import nl.novi.nepplaats.repository.ProductPostRepository;
import nl.novi.nepplaats.repository.StatusRepository;
import nl.novi.nepplaats.service.GebruikerService;
import nl.novi.nepplaats.service.ProductPostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
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
class ProductPostServiceTest {

    @Mock
    private ProductPostRepository productPostRepository;

    @Mock
    private GebruikerService gebruikerService;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private ProductPostService productPostService;

    private ProductPost testProduct;
    private Status statusBeschikbaar;
    private Status statusGereserveerd;
    private Gebruiker eigenaar;


    @BeforeEach
    void setUp() {
        statusBeschikbaar = new Status();
        statusBeschikbaar.setStatusId(1L);
        statusBeschikbaar.setStatusNaam("Beschikbaar");

        statusGereserveerd = new Status();
        statusGereserveerd.setStatusId(2L);
        statusGereserveerd.setStatusNaam("Gereserveerd");

        eigenaar = new Gebruiker();
        eigenaar.setGebruikerId(10L);
        eigenaar.setKeycloakId("keycloak-user-123");

        testProduct = new ProductPost();
        testProduct.setProductPostId(100L);
        testProduct.setTitel("Gitaar");
        testProduct.setPoster(eigenaar);
        testProduct.setStatus(statusBeschikbaar);

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
    @DisplayName("1. Reserveren: Zet status succesvol om van Beschikbaar (1) naar Gereserveerd (2)")
    void reserveer_MetStatusBeschikbaar_ZetStatusOpGereserveerd() {
        when(productPostRepository.findById(100L)).thenReturn(Optional.of(testProduct));
        when(statusRepository.findById(2L)).thenReturn(Optional.of(statusGereserveerd));
        when(productPostRepository.save(any(ProductPost.class))).thenAnswer(i -> i.getArgument(0));

        ProductPostDto.Response result = productPostService.reserveerProductPost(100L);

        assertNotNull(result);
        assertEquals(2L, testProduct.getStatus().getStatusId());
        verify(productPostRepository, times(1)).save(testProduct);
    }

    @Test
    @DisplayName("2. Reserveren: Foutmelding als product al Gereserveerd of Verkocht is")
    void reserveer_ProductAlGereserveerd_GooitIllegalStateException() {
        testProduct.setStatus(statusGereserveerd);
        when(productPostRepository.findById(100L)).thenReturn(Optional.of(testProduct));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            productPostService.reserveerProductPost(100L);
        });

        assertTrue(exception.getMessage().contains("alleen gereserveerd worden als het de status 'Beschikbaar'"));
        verify(productPostRepository, never()).save(any());
    }

    @Test
    @DisplayName("3. Eigenaar: Eigenaar van de advertentie mag de post verwijderen")
    void delete_EigenaarVerwijderdEigenPost_Slaagt() {
        // ARRANGE
        Long postId = 100L;

        // 1. Mock Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);

        // 2. Mock dat het product gevonden wordt
        when(productPostRepository.findById(postId)).thenReturn(Optional.of(testProduct));

        // 3. Mock dat de ingelogde gebruiker de eigenaar is
        when(gebruikerService.getOrCreateGebruikerFromJwt(jwt)).thenReturn(eigenaar);

        // ACT & ASSERT
        assertDoesNotThrow(() -> productPostService.deleteProductPost(postId));

        // VERIFY
        verify(productPostRepository, times(1)).deleteById(postId);
    }

    @Test
    @DisplayName("4. Beveiliging: Niet-eigenaar mag andermans post NIET verwijderen")
    void delete_NietEigenaarProbeertTeVerwijderen_GooitSecurityException() {
        // ARRANGE
        Long postId = 100L;

        // 1. Maak een niet-eigenaar gebruiker aan (bijv. ID 99L met rol USER - ID 2L)
        Rol rolUser = new Rol();
        rolUser.setRolId(2L);
        rolUser.setRolNaam("USER");

        Gebruiker nietEigenaar = new Gebruiker();
        nietEigenaar.setGebruikerId(99L);
        nietEigenaar.setKeycloakId("andere-gebruiker-456");
        nietEigenaar.setRol(rolUser);

        // 2. Mock de SecurityContext en Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);

        // 3. Mock dat het product uit het repository gehaald wordt (van 'eigenaar' met ID 10L)
        when(productPostRepository.findById(postId)).thenReturn(Optional.of(testProduct));

        // 4. Mock dat de GebruikerService de 'nietEigenaar' teruggeeft op basis van het JWT
        when(gebruikerService.getOrCreateGebruikerFromJwt(jwt)).thenReturn(nietEigenaar);

        // ACT & ASSERT
        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> productPostService.deleteProductPost(postId)
        );

        assertEquals("Je bent niet gemachtigd om deze advertentie te verwijderen.", exception.getMessage());

        // Verifieer dat deleteById NOOIT is aangeroepen
        verify(productPostRepository, never()).deleteById(postId);
    }

    @Test
    @DisplayName("5. Autorisatie: ADMIN mag wél andermans post verwijderen")
    void delete_AdminVerwijderdAndermansPost_Slaagt() {
        // ARRANGE
        Long postId = 100L;

        // 1. Maak een Admin gebruiker aan (rolId = 1L)
        Rol rolAdmin = new Rol();
        rolAdmin.setRolId(1L);
        rolAdmin.setRolNaam("ADMIN");

        Gebruiker adminGebruiker = new Gebruiker();
        adminGebruiker.setGebruikerId(999L); // Ander ID dan de eigenaar van testProduct (10L)
        adminGebruiker.setKeycloakId("admin-keycloak-id");
        adminGebruiker.setRol(rolAdmin);

        // 2. Mock Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);

        // 3. Mock dat het product gevonden wordt
        when(productPostRepository.findById(postId)).thenReturn(Optional.of(testProduct));

        // 4. Mock dat de ingelogde gebruiker de ADMIN is
        when(gebruikerService.getOrCreateGebruikerFromJwt(jwt)).thenReturn(adminGebruiker);

        // ACT & ASSERT
        assertDoesNotThrow(() -> productPostService.deleteProductPost(postId));

        // VERIFY
        verify(productPostRepository, times(1)).deleteById(postId);
    }

    @Test
    @DisplayName("6. Foutafhandeling: Verwijderen van niet-bestaande post gooit RecordNotFoundException")
    void delete_NietBestaandProduct_GooitRecordNotFoundException() {
        // ARRANGE: Mock dat findById een lege Optional teruggeeft
        when(productPostRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT: Verwacht dat de exception wordt gegooid
        assertThrows(RecordNotFoundException.class, () -> {
            productPostService.deleteProductPost(999L);
        });

        // VERIFY: Controleer dat deleteById nooit is aangeroepen
        verify(productPostRepository, never()).deleteById(any());
    }
}