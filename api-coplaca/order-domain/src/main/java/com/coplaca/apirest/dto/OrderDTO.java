package com.coplaca.apirest.dto;

import com.coplaca.apirest.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "OrderDTO", description = "Representación completa de un pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    @Schema(description = "ID del pedido", example = "101")
    private Long id;
    @Schema(description = "Número interno del pedido", example = "ORD-AB12CD34")
    private String orderNumber;
    @Schema(description = "ID del cliente", example = "5")
    private Long customerId;
    @Schema(description = "ID del almacén responsable", example = "1")
    private Long warehouseId;
    @Schema(description = "Estado actual del pedido")
    private OrderStatus status;
    @Schema(description = "Total del pedido", example = "12.50")
    private BigDecimal totalPrice;
    @Schema(description = "Subtotal sin envío", example = "9.99")
    private BigDecimal subtotal;
    @Schema(description = "Descuento aplicado", example = "0.00")
    private BigDecimal discount;
    @Schema(description = "Tarifa de envío", example = "4.99")
    private BigDecimal deliveryFee;
    @ArraySchema(arraySchema = @Schema(description = "Líneas de pedido"))
    private List<OrderItemDTO> items;
    @Schema(description = "ID del repartidor asignado", example = "12")
    private Long deliveryAgentId;
    @Schema(description = "Nombre del repartidor asignado", example = "Carlos Martínez")
    private String deliveryAgentName;
    @Schema(description = "ID de la dirección de entrega", example = "7")
    private Long deliveryAddressId;
    @Schema(description = "Dirección completa de entrega")
    private AddressDTO deliveryAddress;
    @Schema(description = "Etiqueta corta de la dirección de entrega", example = "Apto 3B")
    private String deliveryAddressLabel;
    @Schema(description = "Hora estimada de entrega", example = "2026-04-07T12:45:00")
    private LocalDateTime estimatedDeliveryTime;
    @Schema(description = "Hora real de entrega", example = "2026-04-07T12:32:00")
    private LocalDateTime actualDeliveryTime;
    @Schema(description = "Método de pago", example = "CARD")
    private String paymentMethod;
    @Schema(description = "Estado del pago", example = "PENDING")
    private String paymentStatus;
    @Schema(description = "Fecha de creación", example = "2026-04-07T11:10:00")
    private LocalDateTime createdAt;
    @Schema(description = "Fecha de última actualización", example = "2026-04-07T11:20:00")
    private LocalDateTime updatedAt;
}
