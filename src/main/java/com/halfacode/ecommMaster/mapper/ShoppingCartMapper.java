package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.CartItemDTO;
import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.dto.ShoppingCartDTO;
import com.halfacode.ecommMaster.dto.UserDto;
import com.halfacode.ecommMaster.models.CartItem;
import com.halfacode.ecommMaster.models.ShoppingCart;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShoppingCartMapper {

    private final UserMapper userMapper;
    private final CartItemMapper cartItemMapper;

    public ShoppingCartMapper(UserMapper userMapper, CartItemMapper cartItemMapper) {
        this.userMapper = userMapper;
        this.cartItemMapper = cartItemMapper;
    }

    public ShoppingCartDTO toDTO(ShoppingCart shoppingCart) {
        if (shoppingCart == null) {
            return null;
        }

        // Calculate price breakdown
        double subtotal = shoppingCart.getTotalPrice();
        double shippingCost = calculateShipping(subtotal);
        double tax = calculateTax(subtotal);
        double discount = calculateDiscount(shoppingCart);
        double totalPrice = subtotal + shippingCost + tax - discount;

        return ShoppingCartDTO.builder()
                .id(shoppingCart.getId())
                .items(shoppingCart.getItems() != null
                        ? shoppingCart.getItems().stream()
                        .map(cartItemMapper::toDTO)
                        .collect(Collectors.toList())
                        : null)
                .user(shoppingCart.getUser() != null
                        ? UserDto.builder()
                        .username(shoppingCart.getUser().getUsername())
                        .build()
                        : null)

                .subtotal(subtotal)
                .shippingCost(shippingCost)
                .tax(tax)
                .discount(discount)
                .totalPrice(totalPrice)
                .build();
    }

    public ShoppingCart toEntity(ShoppingCartDTO shoppingCartDTO) {
        if (shoppingCartDTO == null) {
            return null;
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartDTO.getId());
        shoppingCart.setItems(shoppingCartDTO.getItems() != null
                ? shoppingCartDTO.getItems().stream()
                .map(cartItemMapper::toEntity)
                .collect(Collectors.toList())
                : null);
        shoppingCart.setUser(shoppingCartDTO.getUser() != null
                ? userMapper.toEntity(shoppingCartDTO.getUser())
                : null);

        return shoppingCart;
    }

    private double calculateShipping(double subtotal) {
        return subtotal > 500 ? 0 : 20; // Free shipping for orders over $500
    }

    private double calculateTax(double subtotal) {
        return subtotal * 0.1; // Assume 10% tax
    }
    private CartItemDTO mapCartItem(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .product(ProductDTO.builder()
                        .id(cartItem.getProduct().getId())
                        .name(cartItem.getProduct().getName())
                        .price(cartItem.getProduct().getPrice())
                        .build()) // Only setting necessary fields
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getTotalPrice())
                .build();
    }


    private double calculateDiscount(ShoppingCart cart) {
        return cart.getTotalPrice() > 1000 ? 50 : 0; // $50 discount for orders above $1000
    }
}
