package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.CartItemDTO;
import com.halfacode.ecommMaster.dto.OrderItemDTO;
import com.halfacode.ecommMaster.models.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItemDTO toDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .product(ProductMapper.toDTO(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }

    public CartItem toEntity(CartItemDTO cartItemDTO) {
        return CartItem.builder()
                .id(cartItemDTO.getId())
                .product(ProductMapper.toEntity(cartItemDTO.getProduct()))
                .quantity(cartItemDTO.getQuantity())
                .totalPrice(cartItemDTO.getTotalPrice())
                .build();
    }

    public static OrderItemDTO toOrderItemDTO(CartItem cartItem) {
        return OrderItemDTO.builder()
                .id(cartItem.getId())
                .productName(cartItem.getProduct().getName())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
