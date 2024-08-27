package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.CartItemDTO;
import com.halfacode.ecommMaster.dto.OrderItemDTO;
import com.halfacode.ecommMaster.models.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public static CartItemDTO toDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .product(ProductMapper.toDTO(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }

    public static CartItem toEntity(CartItemDTO cartItemDTO) {
        return CartItem.builder()
                .id(cartItemDTO.getId())
                .product(ProductMapper.toEntity(cartItemDTO.getProduct()))
                .quantity(cartItemDTO.getQuantity())
                .totalPrice(cartItemDTO.getTotalPrice())
                .build();
    }

    public static CartItemDTO toOrder(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .product(ProductMapper.toCartDTO(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                // .totalPrice(cartItem.getTotalPrice())
                .build();
    }
    public static OrderItemDTO toOrderItemDTO(CartItem cartItem) {
        return OrderItemDTO.builder()
                .id(cartItem.getId())
                .productName(cartItem.getProduct().getName())  // Map product name directly
                .quantity(cartItem.getQuantity())
                .build();
    }
}