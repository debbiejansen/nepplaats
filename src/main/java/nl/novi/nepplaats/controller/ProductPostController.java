package nl.novi.nepplaats.controller;

import jakarta.validation.Valid;
import nl.novi.nepplaats.dto.productpost.ProductPostDto;
import nl.novi.nepplaats.service.ProductPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productposts")
public class ProductPostController {

    private final ProductPostService service;

    public ProductPostController(ProductPostService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductPostDto.Response>> getAllPosts(
            @RequestParam(required = false) Long categorieId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) BigDecimal maxPrijs) {
        return ResponseEntity.ok(service.getAllPosts(categorieId, statusId, maxPrijs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPostDto.Response> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPostById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ProductPostDto.Response> createPost(@Valid @RequestBody ProductPostDto.Request dto) {
        ProductPostDto.Response created = service.createPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductPostDto.Response> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody ProductPostDto.Request dto) {
        return ResponseEntity.ok(service.updatePost(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        service.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}