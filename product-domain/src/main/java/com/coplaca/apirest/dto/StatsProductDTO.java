package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsProductDTO {
    private Long productId;
    private String productName;
    private Integer totalQuantitySold;
    private Double totalRevenue;
    private Integer numberOfOrders;
    private Integer ranking;
}
