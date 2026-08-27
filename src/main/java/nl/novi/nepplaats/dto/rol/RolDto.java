package nl.novi.nepplaats.dto.rol;

public class RolDto {
    private Long rolId;
    private String rolNaam;
    private String rolBeschrijving;

    public RolDto() {
    }
    public RolDto(String rolNaam, String rolBeschrijving) {
        this.rolNaam = rolNaam;
        this.rolBeschrijving = rolBeschrijving;
    }

    // Constructor
    public RolDto(Long rolId, String rolNaam, String rolBeschrijving) {
        this.rolId = rolId;
        this.rolNaam = rolNaam;
        this.rolBeschrijving = rolBeschrijving;
    }

    // Getters + Setters
    public Long getRolId() { return rolId; }
    public void setRolId(Long rolId) { this.rolId = rolId; }

    public String getRolNaam() { return rolNaam; }
    public void setRolNaam(String rolNaam) { this.rolNaam = rolNaam; }

    public String getRolBeschrijving() { return rolBeschrijving; }
    public void setRolBeschrijving(String rolBeschrijving) { this.rolBeschrijving = rolBeschrijving; }
}