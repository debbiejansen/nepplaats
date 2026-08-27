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
    @GetMapping("/getAll")
    public ResponseEntity<List<CategorieDto>> getAllCategories() {
        List<CategorieDto> categories = categorieService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // POST: Nieuwe categorie toevoegen
    @PostMapping("/create")
    public ResponseEntity<CategorieDto> createCategory(@RequestBody CategorieDto newCategoryDto) {
        CategorieDto createdCategory = categorieService.createCategory(newCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }
}