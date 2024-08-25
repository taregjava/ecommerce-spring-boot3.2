package com.halfacode.ecommMaster.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ShoppingCartDTO {
    private Long id;
    private List<CartItemDTO> items;
    private UserDto user;
    private Double totalPrice;
}

