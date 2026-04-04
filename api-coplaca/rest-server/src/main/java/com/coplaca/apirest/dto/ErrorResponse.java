package com.coplaca.apirest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response wrapper for REST API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Always false for error responses
     */
    @Builder.Default
    private boolean success = false;

    /**
     * HTTP status code
     */
    private int status;

    /**
     * Error code for programmatic handling
     */
    private String code;

    /**
     * Human-readable error message
     */
    private String message;

    /**
     * Detailed error information
     */
    private String detail;

    /**
     * Request path that caused the error
     */
    private String path;

    /**
     * Field-specific errors (for validation)
     */
    private Map<String, String> fieldErrors;

    /**
     * Error timestamp
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // Static factory methods for common errors

    public static ErrorResponse of(int status, String code, String message, String path) {
        return ErrorResponse.builder()
                .success(false)
                .status(status)
                .code(code)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(int status, String code, String message, String detail, String path) {
        return ErrorResponse.builder()
                .success(false)
                .status(status)
                .code(code)
                .message(message)
                .detail(detail)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse withFieldErrors(int status, String code, String message, String path, Map<String, String> fieldErrors) {
        return ErrorResponse.builder()
                .success(false)
                .status(status)
                .code(code)
                .message(message)
                .path(path)
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
