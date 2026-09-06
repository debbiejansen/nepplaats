package nl.novi.nepplaats.service;

import nl.novi.nepplaats.dto.productpost.ProductPostDto;
import nl.novi.nepplaats.model.Categorie;
import nl.novi.nepplaats.model.Gebruiker;
import nl.novi.nepplaats.model.ProductPost;
import nl.novi.nepplaats.model.Status;
import nl.novi.nepplaats.repository.CategorieRepository;
import nl.novi.nepplaats.repository.GebruikerRepository;
import nl.novi.nepplaats.repository.ProductPostRepository;
import nl.novi.nepplaats.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductPostService {

    private final ProductPostRepository repository;
    private final GebruikerRepository gebruikerRepository;
    private final CategorieRepository categorieRepository;
    private final StatusRepository statusRepository;

    public ProductPostService(ProductPostRepository repository,
                              GebruikerRepository gebruikerRepository,
                              CategorieRepository categorieRepository,
                              StatusRepository statusRepository) {
        this.repository = repository;
        this.gebruikerRepository = gebruikerRepository;
        this.categorieRepository = categorieRepository;
        this.statusRepository = statusRepository;
    }

    public List<ProductPostDto.Response> getAllPosts(Long categorieId, Long statusId, BigDecimal maxPrijs) {
        List<ProductPost> posts;

        if (categorieId != null) {
            posts = repository.findByCategorieCategorieId(categorieId);// posts = repository.findByCategorieCategorieId(categorieId);
        } else if (statusId != null) {
            posts = repository.findByStatusStatusId(statusId);
        } else if (maxPrijs != null) {
            posts = repository.findByPrijsLessThanEqual(maxPrijs);
        } else {
            posts = repository.findAll();
        }

        List<ProductPostDto.Response> dtos = new ArrayList<>();
        for (ProductPost post : posts) {
            dtos.add(toResponseDto(post));
        }
        return dtos;
    }

    public ProductPostDto.Response getPostById(Long id) {
        ProductPost post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product niet gevonden met id: " + id));
        return toResponseDto(post);
    }

    public ProductPostDto.Response createPost(ProductPostDto.Request dto) {
        ProductPost post = toEntity(dto);
        ProductPost savedPost = repository.save(post);
        return toResponseDto(savedPost);
    }

    public ProductPostDto.Response updatePost(Long id, ProductPostDto.Request dto) {
        ProductPost existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductPost niet gevonden met id: " + id));

        // Update alleen als het veld is meegegeven in de request
        if (dto.getTitel() != null) {
            existing.setTitel(dto.getTitel());
        }
        if (dto.getBeschrijving() != null) {
            existing.setBeschrijving(dto.getBeschrijving());
        }
        if (dto.getPrijs() != null) {
            existing.setPrijs(dto.getPrijs());
        }
        if (dto.getAfbeelding() != null) {
            existing.setAfbeelding(dto.getAfbeelding());
        }

        // relaties
        if (dto.getPosterId() != null) {
            Gebruiker poster = gebruikerRepository.findById(dto.getPosterId())
                    .orElseThrow(() -> new RuntimeException("Gebruiker niet gevonden met id: " + dto.getPosterId()));
            existing.setPoster(poster);
        }
        if (dto.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                    .orElseThrow(() -> new RuntimeException("Categorie niet gevonden met id: " + dto.getCategorieId()));
            existing.setCategorie(categorie);
        }

        if (dto.getStatusId() != null) {
            Status status = statusRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status niet gevonden met id: " + dto.getStatusId()));
            existing.setStatus(status);
        }

        ProductPost updated = repository.save(existing);
        return toResponseDto(updated);
    }

    public void deletePost(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ProductPost niet gevonden met id: " + id);
        }
        repository.deleteById(id);
    }

    // Helpers methoden voor Mappings
    private ProductPostDto.Response toResponseDto(ProductPost entity) {
        ProductPostDto.Response dto = new ProductPostDto.Response();
        dto.setProductPostId(entity.getProductPostId());
        dto.setTitel(entity.getTitel());
        dto.setBeschrijving(entity.getBeschrijving());
        dto.setPrijs(entity.getPrijs());
        dto.setAfbeelding(entity.getAfbeelding());
        dto.setPostDate(entity.getPostDate());

        // null checks
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
        post.setAfbeelding(dto.getAfbeelding());

        // Fetch and SET poster
        Gebruiker poster = gebruikerRepository.findById(dto.getPosterId())
                .orElseThrow(() -> new RuntimeException("Gebruiker niet gevonden met id: " + dto.getPosterId()));
        post.setPoster(poster);

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