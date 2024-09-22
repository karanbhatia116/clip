package app.clip.commons.exceptions;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String assetClass, String assetId) {
        super(assetClass + " with id " + assetId + " not found!");
    }

    public NotFoundException(String assetClass, String assetId, Throwable cause) {
        super(assetClass + " with id " + assetId + " not found!", cause);
    }
}
