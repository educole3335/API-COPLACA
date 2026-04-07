package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Schema(name = "OrderItemDTO", description = "Línea de pedido con producto y subtotal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    @Schema(description = "ID de la línea", example = "1")
    private Long id;
    @Schema(description = "ID del producto", example = "7")
    private Long productId;
    @Schema(description = "Nombre del producto", example = "Mango")
    private String productName;
    @Schema(description = "Cantidad pedida", example = "2.500")
    private BigDecimal quantity;
    @Schema(description = "Precio unitario", example = "3.80")
    private BigDecimal unitPrice;
    @Schema(description = "Subtotal de la línea", example = "9.50")
    private BigDecimal subtotal;
}
