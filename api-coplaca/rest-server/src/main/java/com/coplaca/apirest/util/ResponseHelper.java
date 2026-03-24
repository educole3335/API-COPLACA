package com.coplaca.apirest.util;

import com.coplaca.apirest.dto.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Helper class for building consistent API success responses
 */
public final class ResponseHelper {

    private ResponseHelper() {
        // Utility class
    }

    /**
     * Create a successful response with data
     */
    public static <T> ResponseEntity<SuccessResponse<T>> ok(T data) {
        return ResponseEntity.ok(SuccessResponse.of(data));
    }

    /**
     * Create a successful response with data and message
     */
    public static <T> ResponseEntity<SuccessResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(SuccessResponse.of(data, message));
    }

    /**
     * Create a successful response with only a message
     */
    public static <T> ResponseEntity<SuccessResponse<T>> okMessage(String message) {
        return ResponseEntity.ok(SuccessResponse.ofMessage(message));
    }

    /**
     * Create a created (201) response with data
     */
    public static <T> ResponseEntity<SuccessResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(data, "Resource created successfully"));
    }

    /**
     * Create a created (201) response with data and message
     */
    public static <T> ResponseEntity<SuccessResponse<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.of(data, message));
    }

    /**
     * Create a no content (204) response
     */
    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Create an accepted (202) response with message
     */
    public static <T> ResponseEntity<SuccessResponse<T>> accepted(String message) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(SuccessResponse.ofMessage(message));
    }
}
