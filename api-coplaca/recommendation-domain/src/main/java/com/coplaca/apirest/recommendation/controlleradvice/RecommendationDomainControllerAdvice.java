package com.coplaca.apirest.recommendation.controlleradvice;

import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.recommendation.controller.RecommendationDomainController;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = RecommendationDomainController.class)
public class RecommendationDomainControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid request");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), "Resource not found");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpectedException(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "Internal server error");
    }

    private ResponseEntity<Map<String, String>> build(HttpStatus status, String message, String defaultMessage) {
        String error = message == null || message.isBlank() ? defaultMessage : message;
        return ResponseEntity.status(status)
                .body(Map.of("error", error, "timestamp", Instant.now().toString()));
    }
}
