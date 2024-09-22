package app.clip.commons.exceptions;

import java.util.UUID;

public class ApplicationException extends Exception {

    private String errorId;

    public ApplicationException(String message) {
        super(message);
        this.errorId = UUID.randomUUID().toString();
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.errorId = UUID.randomUUID().toString();
    }

    public String getErrorId() {
        return errorId;
    }
}
