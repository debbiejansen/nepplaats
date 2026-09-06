package nl.novi.nepplaats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {

    // Standaard constructor zonder message
    public RecordNotFoundException() {
        super();
    }

    // Constructor die een aangepast foutbericht accepteert
    public RecordNotFoundException(String message) {
        super(message);
    }
}