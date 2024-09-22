package app.clip.commons.responses;

public record ErrorResponse (int statusCode, String errorId, Object data) {
}
