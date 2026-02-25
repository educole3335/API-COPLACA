package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long clientId;
    private Long warehouseId;
    private String orderStatus;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private LocalDateTime estimatedDelivery;
    private List<OrderItemDTO> items;
}
