package com.coplaca.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String productName;
    private String productImage;
    private String productStatus;
    private BigDecimal price;
    private Integer stock;
    private Long offerId;
    private String offerReason;
    private BigDecimal discountPercentage;
}
