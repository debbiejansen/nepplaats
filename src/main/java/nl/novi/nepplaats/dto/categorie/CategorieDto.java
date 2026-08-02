package nl.novi.nepplaats.dto.categorie;

public class CategorieDto {
    private Integer categorieId;
    private String categorieNaam;
    private String categorieBeschrijving;

    public CategorieDto() {
    }
    public CategorieDto(String categorieNaam, String categorieBeschrijving) {
        this.categorieNaam = categorieNaam;
        this.categorieBeschrijving = categorieBeschrijving;
    }

    // Constructor
    public CategorieDto(Integer categorieId, String categorieNaam, String categorieBeschrijving) {
        this.categorieId = categorieId;
        this.categorieNaam = categorieNaam;
        this.categorieBeschrijving = categorieBeschrijving;
    }

    // Getters + Setters
    public Integer getCategorieId() { return categorieId; }
    public void setCategorieId(Integer categorieId) { this.categorieId = categorieId; }

    public String getCategorieNaam() { return categorieNaam; }
    public void setCategorieNaam(String categorieNaam) { this.categorieNaam = categorieNaam; }

    public String getCategorieBeschrijving() { return categorieBeschrijving; }
    public void setCategorieBeschrijving(String categorieBeschrijving) { this.categorieBeschrijving = categorieBeschrijving; }
}