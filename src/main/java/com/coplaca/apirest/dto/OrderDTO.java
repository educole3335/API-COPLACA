package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.coplaca.apirest.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private Long warehouseId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal deliveryFee;
    private List<OrderItemDTO> items;
    private Long deliveryAgentId;
    private String deliveryAgentName;
    private Long deliveryAddressId;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
