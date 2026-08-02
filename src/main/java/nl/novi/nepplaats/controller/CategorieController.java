package nl.novi.nepplaats.controller;

import nl.novi.nepplaats.dto.categorie.CategorieDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/categories")

public class CategorieController {

    // nep database voor categorieen
    private static final List<CategorieDto> mockCategories = new ArrayList<>();
    private static int categoryIdCounter = 4;

    // Categorieen toevoegen
    static {
        mockCategories.add(new CategorieDto(1, "Elektronica", "Apparaten en gadgets"));
        mockCategories.add(new CategorieDto(2, "Kleding", "Kledingstukken voor alle leeftijden"));
        mockCategories.add(new CategorieDto(3, "Boeken", "Leesboeken, studieboeken en strips"));
    }

    // GET: Alle categorieen ophalen
    @GetMapping("/getAll")
    public ResponseEntity<List<CategorieDto>> getAllCategories() {
        return ResponseEntity.ok(mockCategories);
    }

    // POST: Nieuwe categorie toevoegen
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategorieDto newCategory) {
        // Check of de naam al bestaat
        for (CategorieDto cat : mockCategories) {
            if (cat.getCategorieNaam().equalsIgnoreCase(newCategory.getCategorieNaam())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Categorie met deze naam bestaat al");
            }
        }

        // Genereer ID en voeg toe
        newCategory.setCategorieId(categoryIdCounter++);
        mockCategories.add(newCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }
}