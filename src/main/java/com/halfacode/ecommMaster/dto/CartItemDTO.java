package com.halfacode.ecommMaster.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDTO {
    private Long id;
    private ProductDTO product;
    private Integer quantity;
    private Double totalPrice;
}
