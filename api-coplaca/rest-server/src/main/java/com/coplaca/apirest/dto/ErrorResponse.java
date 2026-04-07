package com.coplaca.apirest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response wrapper for REST API
 */
@Schema(name = "ErrorResponse", description = "Respuesta estándar de error de la API")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Always false for error responses
     */
    @Schema(description = "Indica que la operación falló", example = "false")
    @Builder.Default
    private boolean success = false;

    /**
     * HTTP status code
     */
    @Schema(description = "Código HTTP", example = "400")
    private int status;

    /**
     * Error code for programmatic handling
     */
    @Schema(description = "Código de error interno", example = "INVALID_REQUEST")
    private String code;

    /**
     * Human-readable error message
     */
    @Schema(description = "Mensaje legible del error", example = "Validation failed for one or more fields")
    private String message;

    /**
     * Detailed error information
     */
    @Schema(description = "Detalle técnico del error", example = "Field 'email' must not be blank")
    private String detail;

    /**
     * Request path that caused the error
     */
    @Schema(description = "Ruta que provocó el error", example = "/auth/login")
    private String path;

    /**
     * Field-specific errors (for validation)
     */
    @Schema(description = "Errores por campo de validación")
    private Map<String, String> fieldErrors;

    /**
     * Error timestamp
     */
    @Schema(description = "Fecha y hora del error", example = "2026-04-07T11:30:00")
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
