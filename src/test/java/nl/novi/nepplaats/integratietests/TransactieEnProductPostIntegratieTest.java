package nl.novi.nepplaats.integratietests;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.nepplaats.dto.transactie.TransactieDto;
import nl.novi.nepplaats.model.Gebruiker;
import nl.novi.nepplaats.model.ProductPost;
import nl.novi.nepplaats.model.Rol;
import nl.novi.nepplaats.model.Status;
import nl.novi.nepplaats.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // <-- DIT HOUDT JE DB EN SEQUENCES SCHOON
public class TransactieEnProductPostIntegratieTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactieRepository transactieRepository;

    @Autowired
    private ProductPostRepository productPostRepository;

    @Autowired
    private GebruikerRepository gebruikerRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private RolRepository rolRepository;

    private ProductPost testProduct;
    private Gebruiker testKoper;

    @BeforeEach
    void setUp() {
        // Statussen opslaan (Omdat de context vers is, start de sequence altijd bij 1)
        Status statusBeschikbaar = new Status();
        statusBeschikbaar.setStatusNaam("Beschikbaar");
        statusBeschikbaar.setStatusBeschrijving("Product staat te koop");
        statusBeschikbaar = statusRepository.save(statusBeschikbaar); // Krijgt ID 1L

        Status statusGereserveerd = new Status();
        statusGereserveerd.setStatusNaam("Gereserveerd");
        statusGereserveerd.setStatusBeschrijving("Product is gereserveerd");
        statusRepository.save(statusGereserveerd); // Krijgt ID 2L

        Status statusVerkocht = new Status();
        statusVerkocht.setStatusNaam("Verkocht");
        statusVerkocht.setStatusBeschrijving("Product is verkocht");
        statusRepository.save(statusVerkocht); // Krijgt ID 3L

        assertEquals(1L, statusBeschikbaar.getStatusId(), "Status Beschikbaar moet ID 1L hebben voor de service check");

        // Rol en Gebruikers opslaan
        Rol rolGebruiker = new Rol();
        rolGebruiker.setRolNaam("ROLE_USER");
        rolGebruiker.setRolBeschrijving("Standaard gebruiker");
        rolGebruiker = rolRepository.save(rolGebruiker);

        Gebruiker verkoper = new Gebruiker();
        verkoper.setGebruikersnaam("verkoper_jan");
        verkoper.setEmail("verkoper@test.com");
        verkoper.setKeycloakId("keycloak-123");
        verkoper.setRol(rolGebruiker);
        verkoper = gebruikerRepository.save(verkoper);

        testKoper = new Gebruiker();
        testKoper.setGebruikersnaam("koper_piet");
        testKoper.setEmail("koper@test.com");
        testKoper.setKeycloakId("keycloak-456");
        testKoper.setRol(rolGebruiker);
        testKoper = gebruikerRepository.save(testKoper);

        // ProductPost opslaan
        testProduct = new ProductPost();
        testProduct.setTitel("Vintage gitaar");
        testProduct.setBeschrijving("In uitstekende staat");
        testProduct.setPrijs(new BigDecimal("250.00"));
        testProduct.setPostDate(java.time.LocalDateTime.now());
        testProduct.setPoster(verkoper);
        testProduct.setStatus(statusBeschikbaar);
        testProduct = productPostRepository.save(testProduct);
    }

    @Test
    @DisplayName("POST /api/transacties - Maakt transactie aan en verandert Product status automatisch naar 3 (Verkocht)")
    void createTransactie_shouldCreateTransactieAndUpdateProductStatusToVerkocht() throws Exception {
        TransactieDto.Request request = new TransactieDto.Request();
        request.setProductPostId(testProduct.getProductPostId());
        request.setKoperId(testKoper.getGebruikerId());

        mockMvc.perform(post("/api/transacties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactieId").exists())
                .andExpect(jsonPath("$.productPostId").value(testProduct.getProductPostId()))
                .andExpect(jsonPath("$.koperId").value(testKoper.getGebruikerId()));

        ProductPost updatedProduct = productPostRepository.findById(testProduct.getProductPostId())
                .orElseThrow(() -> new AssertionError("ProductPost niet gevonden in DB"));

        assertEquals(3L, updatedProduct.getStatus().getStatusId(), "Status van ProductPost moet veranderd zijn naar 3 (Verkocht)");
    }

    @Test
    @DisplayName("PATCH /api/productposts/{id}/reserveer - Verandert de status van ProductPost van 1 naar 2 (Gereserveerd)")
    void reserveerProductPost_shouldUpdateStatusToGereserveerd() throws Exception {
        mockMvc.perform(patch("/api/productposts/{id}/reserveer", testProduct.getProductPostId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productPostId").value(testProduct.getProductPostId()))
                .andExpect(jsonPath("$.statusId").value(2L));

        ProductPost updatedProduct = productPostRepository.findById(testProduct.getProductPostId())
                .orElseThrow(() -> new AssertionError("ProductPost niet gevonden in DB"));

        assertEquals(2L, updatedProduct.getStatus().getStatusId(), "Status in DB moet nu 2 (Gereserveerd) zijn");
    }
}