package nl.novi.nepplaats.service;

import nl.novi.nepplaats.dto.categorie.CategorieDto;
import nl.novi.nepplaats.exception.RecordNotFoundException;
import nl.novi.nepplaats.model.Categorie;
import nl.novi.nepplaats.repository.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    // Haal alle categorieën op en zet ze om naar DTO's
    public List<CategorieDto> getAllCategories() {
        List<Categorie> categories = categorieRepository.findAll();
        List<CategorieDto> categoryDtos = new ArrayList<>();

        for (Categorie categorie : categories) {
            categoryDtos.add(transferToDto(categorie));
        }

        return categoryDtos;
    }

    // Haal één specifieke categorie op op basis van ID
    public CategorieDto getCategoryById(Long id) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Categorie met ID " + id + " niet gevonden"));

        return transferToDto(categorie);
    }

    // Maak een nieuwe categorie aan
    public CategorieDto createCategory(CategorieDto categorieDto) {
        // Zet DTO om naar Entiteit
        Categorie categorie = transferToEntity(categorieDto);

        // Sla de entiteit op in de database via de repository
        Categorie savedCategorie = categorieRepository.save(categorie);

        // Zet het opgeslagen resultaat weer om naar een DTO om terug te sturen
        return transferToDto(savedCategorie);
    }

    // Bestaande categorie aanpassen
    public CategorieDto updateCategory(Long id, CategorieDto inputDto) {
        // 1. Zoek de bestaande entiteit op in de database via de repository
        Categorie existingCategorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Categorie met ID " + id + " niet gevonden"));

        // 2. Pas de velden aan met de waarden uit het binnenkomende DTO
        if (inputDto.getCategorieNaam() != null) {
            existingCategorie.setCategorieNaam(inputDto.getCategorieNaam());
        }
        if (inputDto.getCategorieBeschrijving() != null) {
            existingCategorie.setCategorieBeschrijving(inputDto.getCategorieBeschrijving());
        }

        // 3. Sla de gewijzigde entiteit op
        Categorie updatedCategorie = categorieRepository.save(existingCategorie);

        // 4. Zet om naar DTO en stuur terug
        return transferToDto(updatedCategorie);
    }

    // Verwijder een categorie op basis van ID
    public void deleteCategory(Long id) {
        if (!categorieRepository.existsById(id)) {
            throw new RecordNotFoundException("Categorie met ID " + id + " niet gevonden");
        }

        categorieRepository.deleteById(id);
    }

    // Helper methode: Entiteit -> DTO
    private CategorieDto transferToDto(Categorie categorie) {
        CategorieDto dto = new CategorieDto();
        dto.setCategorieId(categorie.getCategorieId());
        dto.setCategorieNaam(categorie.getCategorieNaam());
        dto.setCategorieBeschrijving(categorie.getCategorieBeschrijving());
        return dto;
    }

    // Helper methode: DTO -> Entiteit
    private Categorie transferToEntity(CategorieDto dto) {
        Categorie categorie = new Categorie();
        categorie.setCategorieNaam(dto.getCategorieNaam());
        categorie.setCategorieBeschrijving(dto.getCategorieBeschrijving());
        return categorie;
    }
}