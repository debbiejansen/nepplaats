package nl.novi.nepplaats.service;

import jakarta.persistence.EntityNotFoundException;
import nl.novi.nepplaats.dto.productpost.ProductPostDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.*;
import nl.novi.nepplaats.repository.*;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductPostService {

    private final ProductPostRepository productPostRepository;
    private final GebruikerRepository gebruikerRepository;
    private final CategorieRepository categorieRepository;
    private final StatusRepository statusRepository;
    private final AfbeeldingRepository afbeeldingRepository;
    private final GebruikerService gebruikerService;

    public ProductPostService(ProductPostRepository productPostRepository,
                              GebruikerRepository gebruikerRepository,
                              CategorieRepository categorieRepository,
                              StatusRepository statusRepository,
                              AfbeeldingRepository afbeeldingRepository,
                              GebruikerService gebruikerService) {
        this.productPostRepository = productPostRepository;
        this.gebruikerRepository = gebruikerRepository;
        this.categorieRepository = categorieRepository;
        this.statusRepository = statusRepository;
        this.afbeeldingRepository = afbeeldingRepository;
        this.gebruikerService = gebruikerService;
    }

    public List<ProductPostDto.Response> getAllProductPosts(Long categorieId, Long statusId, BigDecimal maxPrijs) {
        List<ProductPost> posts;

        if (categorieId != null) {
            posts = productPostRepository.findByCategorie_CategorieId(categorieId);// posts = repository.findByCategorieCategorieId(categorieId);
        } else if (statusId != null) {
            posts = productPostRepository.findByStatus_StatusId(statusId);
        } else if (maxPrijs != null) {
            posts = productPostRepository.findByPrijsLessThanEqual(maxPrijs);
        } else {
            posts = productPostRepository.findAll();
        }

        List<ProductPostDto.Response> dtos = new ArrayList<>();
        for (ProductPost post : posts) {
            dtos.add(toResponseDto(post));
        }
        return dtos;
    }

    public ProductPostDto.Response getProductPostById(Long id) {
        ProductPost post = productPostRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Product niet gevonden met id: " + id));
        return toResponseDto(post);
    }

    @Transactional
    public ProductPostDto.Response createProductPost(ProductPostDto.Request dto) {
        ProductPost post = toEntity(dto);

        // Forceer status 1 (Beschikbaar)
        Status beschikbaarStatus = statusRepository.findById(1L)
                .orElseThrow(() -> new RecordNotFoundException("Status 'Beschikbaar' (id: 1) niet gevonden in database."));
        post.setStatus(beschikbaarStatus);

        if (dto.getAfbeeldingId() != null) {
            Afbeelding afbeelding = afbeeldingRepository.findById(dto.getAfbeeldingId())
                    .orElseThrow(() -> new RecordNotFoundException("Afbeelding niet gevonden met id: " + dto.getAfbeeldingId()));
            post.setAfbeelding(afbeelding);
        }
        if (dto.getPosterId() != null) {
            Gebruiker poster = gebruikerRepository.findById(dto.getPosterId())
                    .orElseThrow(() -> new RecordNotFoundException("Gebruiker niet gevonden met id: " + dto.getPosterId()));
            post.setPoster(poster);
        }
        if (dto.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                    .orElseThrow(() -> new RecordNotFoundException("Categorie niet gevonden met id: " + dto.getCategorieId()));
            post.setCategorie(categorie);
        }

        ProductPost savedPost = productPostRepository.save(post);
        return toResponseDto(savedPost);
    }

    @Transactional
    public ProductPostDto.Response reserveerProductPost(Long id) {
        ProductPost post = productPostRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("ProductPost niet gevonden met id: " + id));

        // Controleer of het product wel gereserveerd kán worden (moet momenteel status 1 hebben)
        if (post.getStatus() == null || post.getStatus().getStatusId() != 1L) {
            throw new IllegalStateException("ProductPost kan alleen gereserveerd worden als het de status 'Beschikbaar' (1) heeft.");
        }

        Status gereserveerdStatus = statusRepository.findById(2L)
                .orElseThrow(() -> new RecordNotFoundException("Status 'Gereserveerd' (id: 2) niet gevonden in database."));
        post.setStatus(gereserveerdStatus);

        ProductPost savedPost = productPostRepository.save(post);
        return toResponseDto(savedPost);
    }

    public ProductPostDto.Response updateProductPost(Long id, ProductPostDto.Request dto) {
        ProductPost existingProductPost = productPostRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("ProductPost niet gevonden met id: " + id));

        // Update alleen als het veld is meegegeven in de request
        if (dto.getTitel() != null) {
            existingProductPost.setTitel(dto.getTitel());
        }
        if (dto.getBeschrijving() != null) {
            existingProductPost.setBeschrijving(dto.getBeschrijving());
        }
        if (dto.getPrijs() != null) {
            existingProductPost.setPrijs(dto.getPrijs());
        }
        if (dto.getAfbeeldingId() != null) {
            Afbeelding afbeelding = afbeeldingRepository.findById(dto.getAfbeeldingId())
                    .orElseThrow(() -> new RecordNotFoundException("Afbeelding niet gevonden met id: " + dto.getAfbeeldingId()));
            existingProductPost.setAfbeelding(afbeelding);
        }

        // relaties
        if (dto.getPosterId() != null) {
            Gebruiker poster = gebruikerRepository.findById(dto.getPosterId())
                    .orElseThrow(() -> new RecordNotFoundException("Gebruiker niet gevonden met id: " + dto.getPosterId()));
            existingProductPost.setPoster(poster);
        }
        if (dto.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                    .orElseThrow(() -> new RecordNotFoundException("Categorie niet gevonden met id: " + dto.getCategorieId()));
            existingProductPost.setCategorie(categorie);
        }
        if (dto.getStatusId() != null) {
            Status status = statusRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new RecordNotFoundException("Status niet gevonden met id: " + dto.getStatusId()));
            existingProductPost.setStatus(status);
        }

        ProductPost updated = productPostRepository.save(existingProductPost);
        return toResponseDto(updated);
    }


    public void deleteProductPost(Long id) {
        // 1. Zoek het product op
        ProductPost post = productPostRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Advertentie niet gevonden met id: " + id));

        // 2. Haal het Jwt object op uit de SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("Geen geldige JWT authenticatie gevonden.");
        }

        // 3. Haal de database Gebruiker-entiteit op via jouw bestaande GebruikerService
        Gebruiker ingelogdeGebruiker = gebruikerService.getOrCreateGebruikerFromJwt(jwt);

        // 4. Controleer de rechten
        boolean isEigenaar = post.getPoster() != null &&
                post.getPoster().getGebruikerId().equals(ingelogdeGebruiker.getGebruikerId());

        // Controleer of de rol ADMIN is (Rol ID 1L)
        boolean isAdmin = ingelogdeGebruiker.getRol() != null &&
                Long.valueOf(1L).equals(ingelogdeGebruiker.getRol().getRolId());

        if (!isEigenaar && !isAdmin) {
            throw new AccessDeniedException("Je bent niet gemachtigd om deze advertentie te verwijderen.");
        }

        // 5. Verwijderen
        productPostRepository.deleteById(id);
    }

    // Helpers methoden voor Mappings
    private ProductPostDto.Response toResponseDto(ProductPost entity) {
        ProductPostDto.Response dto = new ProductPostDto.Response();
        dto.setProductPostId(entity.getProductPostId());
        dto.setTitel(entity.getTitel());
        dto.setBeschrijving(entity.getBeschrijving());
        dto.setPrijs(entity.getPrijs());
        dto.setPostDate(entity.getPostDate());

        // null checks
        if (entity.getAfbeelding() != null) {
            dto.setAfbeeldingId(entity.getAfbeelding().getAfbeeldingId());
        }
        if (entity.getPoster() != null) {
            dto.setPosterId(entity.getPoster().getGebruikerId());
        }
        if (entity.getCategorie() != null) {
            dto.setCategorieId(entity.getCategorie().getCategorieId());
        }
        if (entity.getStatus() != null) {
            dto.setStatusId(entity.getStatus().getStatusId());
        }

        return dto;
    }

    private ProductPost toEntity(ProductPostDto.Request dto) {
        ProductPost post = new ProductPost();
        post.setTitel(dto.getTitel());
        post.setBeschrijving(dto.getBeschrijving());
        post.setPrijs(dto.getPrijs());

        // Fetch and SET poster
        Gebruiker poster = gebruikerRepository.findById(dto.getPosterId())
                .orElseThrow(() -> new RecordNotFoundException("Gebruiker niet gevonden met id: " + dto.getPosterId()));
        post.setPoster(poster);

        // Fetch and SET afbeelding
        if (dto.getAfbeeldingId() != null) {
            Afbeelding afbeelding = afbeeldingRepository.findById(dto.getAfbeeldingId())
                    .orElseThrow(() -> new RecordNotFoundException("Afbeelding niet gevonden met id: " + dto.getAfbeeldingId()));
            post.setAfbeelding(afbeelding);
        }

        // Fetch and SET categorie
        if (dto.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                    .orElseThrow(() -> new RecordNotFoundException("Categorie niet gevonden met id: " + dto.getCategorieId()));
            post.setCategorie(categorie);
        }

        // Fetch and SET status
        if (dto.getStatusId() != null) {
            Status status = statusRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new RecordNotFoundException("Status niet gevonden met id: " + dto.getStatusId()));
            post.setStatus(status);
        }

        return post;
    }
}