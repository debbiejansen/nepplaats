package nl.novi.nepplaats.service;

import nl.novi.nepplaats.dto.productpost.ProductPostDto;
import nl.novi.nepplaats.model.ProductPost;
import nl.novi.nepplaats.repository.ProductPostRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductPostService {

    private final ProductPostRepository repository;

    public ProductPostService(ProductPostRepository repository) {
        this.repository = repository;
    }

    public List<ProductPostDto.Response> getAllPosts(Long categorieId, Long statusId, BigDecimal maxPrijs) {
        List<ProductPost> posts;

        if (categorieId != null) {
            posts = repository.findByCategorieId(categorieId);
        } else if (statusId != null) {
            posts = repository.findByStatusId(statusId);
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

        existing.setTitel(dto.getTitel());
        existing.setBeschrijving(dto.getBeschrijving());
        existing.setPrijs(dto.getPrijs());
        existing.setAfbeelding(dto.getAfbeelding());
        existing.setPosterId(dto.getPosterId());
        existing.setCategorieId(dto.getCategorieId());
        existing.setStatusId(dto.getStatusId());

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
        dto.setPosterId(entity.getPosterId());
        dto.setCategorieId(entity.getCategorieId());
        dto.setStatusId(entity.getStatusId());
        return dto;
    }

    private ProductPost toEntity(ProductPostDto.Request dto) {
        ProductPost post = new ProductPost();
        post.setTitel(dto.getTitel());
        post.setBeschrijving(dto.getBeschrijving());
        post.setPrijs(dto.getPrijs());
        post.setAfbeelding(dto.getAfbeelding());
        post.setPosterId(dto.getPosterId());
        post.setCategorieId(dto.getCategorieId());
        post.setStatusId(dto.getStatusId());
        return post;
    }

}
