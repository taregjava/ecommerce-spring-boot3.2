package com.halfacode.ecommMaster.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    private Long id;
    private String productName;  // Change the field to productName
    private Integer quantity;
}

