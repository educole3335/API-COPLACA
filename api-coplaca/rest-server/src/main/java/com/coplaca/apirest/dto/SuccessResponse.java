package com.coplaca.apirest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard success response wrapper for REST API
 * @param <T> The type of data being returned
 */
@Schema(name = "SuccessResponse", description = "Respuesta estándar de éxito de la API")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> {

    /**
     * Always true for success responses
     */
    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    @Builder.Default
    private boolean success = true;

    /**
     * Optional success message
     */
    @Schema(description = "Mensaje descriptivo de la operación", example = "Resource created successfully")
    private String message;

    /**
     * The actual response data
     */
    @Schema(description = "Datos devueltos por la operación")
    private T data;

    /**
     * Response timestamp
     */
    @Schema(description = "Fecha y hora de la respuesta", example = "2026-04-07T11:30:00")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Pagination metadata (optional)
     */
    @Schema(description = "Metadatos de paginación cuando aplica")
    private PaginationMetadata pagination;

    // Static factory methods for common responses

    public static <T> SuccessResponse<T> of(T data) {
        return SuccessResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> SuccessResponse<T> of(T data, String message) {
        return SuccessResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> SuccessResponse<T> ofMessage(String message) {
        return SuccessResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "PaginationMetadata", description = "Metadatos de paginación opcionales")
    public static class PaginationMetadata {
        @Schema(description = "Página actual", example = "1")
        private int page;
        @Schema(description = "Tamaño de página", example = "20")
        private int pageSize;
        @Schema(description = "Total de elementos", example = "145")
        private long totalElements;
        @Schema(description = "Total de páginas", example = "8")
        private int totalPages;
        @Schema(description = "Indica si hay siguiente página", example = "true")
        private boolean hasNext;
        @Schema(description = "Indica si hay página anterior", example = "false")
        private boolean hasPrevious;
    }
}
