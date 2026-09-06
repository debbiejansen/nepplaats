package nl.novi.nepplaats.dto.productpost;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductPostDto {

    // input DTO voor aanmaken/bijwerken
    public static class Request {
        @NotBlank(message = "Titel is verplicht")
        private String titel;
        private String beschrijving;

        @NotNull(message = "Prijs is verplicht")
        @Positive(message = "Prijs moet hoger zijn dan 0")
        private BigDecimal prijs;
        private String afbeelding;

        @NotNull(message = "Poster ID is verplicht")
        private Long posterId;
        private Long categorieId;
        private Long statusId;

        // GETTERS EN SETTERS
        public String getTitel() { return titel; }
        public void setTitel(String titel) { this.titel = titel; }

        public String getBeschrijving() { return beschrijving; }
        public void setBeschrijving(String beschrijving) { this.beschrijving = beschrijving; }

        public BigDecimal getPrijs() { return prijs; }
        public void setPrijs(BigDecimal prijs) { this.prijs = prijs; }

        public String getAfbeelding() { return afbeelding; }
        public void setAfbeelding(String afbeelding) { this.afbeelding = afbeelding; }

        public Long getPosterId() { return posterId; }
        public void setPosterId(Long posterId) { this.posterId = posterId; }

        public Long getCategorieId() { return categorieId; }
        public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }

        public Long getStatusId() { return statusId; }
        public void setStatusId(Long statusId) { this.statusId = statusId; }
    }

    // Output DTO
    public static class Response {
        private Long productPostId;
        private String titel;
        private String beschrijving;
        private BigDecimal prijs;
        private String afbeelding;
        private LocalDateTime postDate;
        private Long posterId;
        private Long categorieId;
        private Long statusId;

        // Getters & Setters
        public Long getProductPostId() { return productPostId; }
        public void setProductPostId(Long productPostId) { this.productPostId = productPostId; }

        public String getTitel() { return titel; }
        public void setTitel(String titel) { this.titel = titel; }

        public String getBeschrijving() { return beschrijving; }
        public void setBeschrijving(String beschrijving) { this.beschrijving = beschrijving; }

        public BigDecimal getPrijs() { return prijs; }
        public void setPrijs(BigDecimal prijs) { this.prijs = prijs; }

        public String getAfbeelding() { return afbeelding; }
        public void setAfbeelding(String afbeelding) { this.afbeelding = afbeelding; }

        public LocalDateTime getPostDate() { return postDate; }
        public void setPostDate(LocalDateTime postDate) { this.postDate = postDate; }

        public Long getPosterId() { return posterId; }
        public void setPosterId(Long posterId) { this.posterId = posterId; }

        public Long getCategorieId() { return categorieId; }
        public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }

        public Long getStatusId() { return statusId; }
        public void setStatusId(Long statusId) { this.statusId = statusId; }

    }
}