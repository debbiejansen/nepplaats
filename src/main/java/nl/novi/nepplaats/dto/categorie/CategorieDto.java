package nl.novi.nepplaats.dto.categorie;

public class CategorieDto {
    private Long categorieId;
    private String categorieNaam;
    private String categorieBeschrijving;

    public CategorieDto() {
    }
    public CategorieDto(String categorieNaam, String categorieBeschrijving) {
        this.categorieNaam = categorieNaam;
        this.categorieBeschrijving = categorieBeschrijving;
    }

    // Constructor
    public CategorieDto(Long categorieId, String categorieNaam, String categorieBeschrijving) {
        this.categorieId = categorieId;
        this.categorieNaam = categorieNaam;
        this.categorieBeschrijving = categorieBeschrijving;
    }

    // Getters + Setters
    public Long getCategorieId() { return categorieId; }
    public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }

    public String getCategorieNaam() { return categorieNaam; }
    public void setCategorieNaam(String categorieNaam) { this.categorieNaam = categorieNaam; }

    public String getCategorieBeschrijving() { return categorieBeschrijving; }
    public void setCategorieBeschrijving(String categorieBeschrijving) { this.categorieBeschrijving = categorieBeschrijving; }
}