package nl.novi.nepplaats.controller;

import nl.novi.nepplaats.dto.categorie.CategorieDto;
import nl.novi.nepplaats.service.CategorieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categorieen")
public class CategorieController {

    private final CategorieService categorieService;

    // Injecteer de Service via de constructor
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    // GET: Alle categorieën ophalen
    @GetMapping
    public ResponseEntity<List<CategorieDto>> getAllCategories() {
        return ResponseEntity.ok(categorieService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.getCategoryById(id));
    }

    // POST: Nieuwe categorie toevoegen
    @PostMapping
    public ResponseEntity<CategorieDto> createCategory(@RequestBody CategorieDto categoryDto) {
        CategorieDto created = categorieService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT: Bestaande categorie aanpassen op basis van ID
    @PutMapping("/{id}")
    public ResponseEntity<CategorieDto> updateCategory(@PathVariable Long id, @RequestBody CategorieDto categoryDto) {
        return ResponseEntity.ok(categorieService.updateCategory(id, categoryDto));
    }

    // DELETE: Bestaande categorie verwijderen op basis van ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categorieService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}