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
        return ResponseEntity.ok(service.getAllProductPosts(categorieId, statusId, maxPrijs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPostDto.Response> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductPostById(id));
    }

    @PostMapping
    public ResponseEntity<ProductPostDto.Response> createPost(@Valid @RequestBody ProductPostDto.Request dto) {
        ProductPostDto.Response created = service.createProductPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/reserveer")
    public ResponseEntity<ProductPostDto.Response> reserveerPost(@PathVariable Long id) {
        return ResponseEntity.ok(service.reserveerProductPost(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductPostDto.Response> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody ProductPostDto.Request dto) {
        return ResponseEntity.ok(service.updateProductPost(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        service.deleteProductPost(id);
        return ResponseEntity.noContent().build();
    }
}