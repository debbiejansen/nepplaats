package nl.novi.nepplaats.service;

import nl.novi.nepplaats.dto.productpost.ProductPostDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.*;
import nl.novi.nepplaats.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductPostService {

    private final ProductPostRepository productPostRepository;
    private final GebruikerRepository gebruikerRepository;
    private final CategorieRepository categorieRepository;
    private final StatusRepository statusRepository;
    private final AfbeeldingRepository afbeeldingRepository;

    public ProductPostService(ProductPostRepository productPostRepository,
                              GebruikerRepository gebruikerRepository,
                              CategorieRepository categorieRepository,
                              StatusRepository statusRepository,
                              AfbeeldingRepository afbeeldingRepository) {
        this.productPostRepository = productPostRepository;
        this.gebruikerRepository = gebruikerRepository;
        this.categorieRepository = categorieRepository;
        this.statusRepository = statusRepository;
        this.afbeeldingRepository = afbeeldingRepository;
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
                .orElseThrow(() -> new RuntimeException("Product niet gevonden met id: " + id));
        return toResponseDto(post);
    }

    public ProductPostDto.Response createProductPost(ProductPostDto.Request dto) {
        ProductPost post = toEntity(dto);
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
                    .orElseThrow(() -> new RuntimeException("Afbeelding niet gevonden met id: " + dto.getAfbeeldingId()));
            existingProductPost.setAfbeelding(afbeelding);
        }

        // relaties
        if (dto.getPosterId() != null) {
            Gebruiker poster = gebruikerRepository.findById(dto.getPosterId())
                    .orElseThrow(() -> new RuntimeException("Gebruiker niet gevonden met id: " + dto.getPosterId()));
            existingProductPost.setPoster(poster);
        }
        if (dto.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                    .orElseThrow(() -> new RuntimeException("Categorie niet gevonden met id: " + dto.getCategorieId()));
            existingProductPost.setCategorie(categorie);
        }
        if (dto.getStatusId() != null) {
            Status status = statusRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status niet gevonden met id: " + dto.getStatusId()));
            existingProductPost.setStatus(status);
        }

        ProductPost updated = productPostRepository.save(existingProductPost);
        return toResponseDto(updated);
    }

    public void deleteProductPost(Long id) {
        if (!productPostRepository.existsById(id)) {
            throw new RuntimeException("ProductPost niet gevonden met id: " + id);
        }
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
                .orElseThrow(() -> new RuntimeException("Gebruiker niet gevonden met id: " + dto.getPosterId()));
        post.setPoster(poster);

        // Fetch and SET afbeelding
        if (dto.getAfbeeldingId() != null) {
            Afbeelding afbeelding = afbeeldingRepository.findById(dto.getAfbeeldingId())
                    .orElseThrow(() -> new RuntimeException("Afbeelding niet gevonden met id: " + dto.getAfbeeldingId()));
            post.setAfbeelding(afbeelding);
        }

        // Fetch and SET categorie
        if (dto.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                    .orElseThrow(() -> new RuntimeException("Categorie niet gevonden met id: " + dto.getCategorieId()));
            post.setCategorie(categorie);
        }

        // Fetch and SET status
        if (dto.getStatusId() != null) {
            Status status = statusRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status niet gevonden met id: " + dto.getStatusId()));
            post.setStatus(status);
        }

        return post;
    }
}