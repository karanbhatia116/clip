package app.clip.handlers;

import app.clip.commons.exceptions.ApplicationException;
import app.clip.commons.exceptions.InvalidRequestException;
import app.clip.commons.exceptions.NotFoundException;
import app.clip.commons.exceptions.ValidationException;
import app.clip.commons.responses.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        ErrorResponse errorResponse = buildErrorResponse(exception);
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(errorResponse.statusCode()));
    }

    private ErrorResponse buildErrorResponse(final Exception exception) {
        return switch (exception) {
            case NotFoundException e -> {
                LOGGER.error("Error message: {}  Error ID: {}", e.getMessage(), e.getErrorId(), e);
                yield new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getErrorId(), e.getMessage());
            }
            case ValidationException e -> {
                LOGGER.warn("Error message: {}  Error ID: {}", e.getMessage(), e.getErrorId(), e);
                yield new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getErrorId(), e.getViolations());
            }
            case InvalidRequestException e -> {
                LOGGER.warn("Error message: {}  Error ID: {}", e.getMessage(), e.getErrorId(), e);
                yield new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getErrorId(), e.getMessage());
            }
            case ApplicationException e -> {
                LOGGER.error("Error message: {}  Error ID: {}", e.getMessage(), e.getErrorId(), e);
                yield new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getErrorId(), e.getMessage());
            }
            default -> {
                String randomUUID = UUID.randomUUID().toString();
                LOGGER.error("Error message: {}  Error ID: {}", exception.getMessage(), randomUUID, exception);
                yield new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), randomUUID, exception.getMessage());
            }
        };
    }
}
