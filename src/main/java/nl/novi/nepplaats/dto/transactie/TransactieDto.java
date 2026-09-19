package nl.novi.nepplaats.dto.transactie;

import java.time.LocalDateTime;

public class TransactieDto {

    // Request DTO (voor het aanmaken van een transactie)
    public static class Request {
        private Long originalProductPostId;
        private Long koperId;

        public Long getOriginalProductPostId() {
            return originalProductPostId;
        }

        public void setOriginalProductPostId(Long originalProductPostId) {
            this.originalProductPostId = originalProductPostId;
        }

        public Long getKoperId() {
            return koperId;
        }

        public void setKoperId(Long koperId) {
            this.koperId = koperId;
        }
    }

    // Response DTO (voor het terugsturen van gegevens naar de client)
    public static class Response {
        private Long transactieId;
        private Long originalProductPostId;
        private Long koperId;
        private LocalDateTime tijd;

        public Long getTransactieId() {
            return transactieId;
        }

        public void setTransactieId(Long transactieId) {
            this.transactieId = transactieId;
        }

        public Long getOriginalProductPostId() {
            return originalProductPostId;
        }

        public void setOriginalProductPostId(Long originalProductPostId) {
            this.originalProductPostId = originalProductPostId;
        }

        public Long getKoperId() {
            return koperId;
        }

        public void setKoperId(Long koperId) {
            this.koperId = koperId;
        }

        public LocalDateTime getTijd() {
            return tijd;
        }

        public void setTijd(LocalDateTime tijd) {
            this.tijd = tijd;
        }
    }
}