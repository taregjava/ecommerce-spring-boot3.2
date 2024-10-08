package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.ShoppingCartDTO;
import com.halfacode.ecommMaster.models.ShoppingCart;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShoppingCartMapper {


    private final UserMapper userMapper;

    public ShoppingCartMapper(UserMapper userMapper) {
    //    this.cartItemMapper = cartItemMapper;
        this.userMapper = userMapper;
    }

    public ShoppingCartDTO toDTO(ShoppingCart shoppingCart) {
        return ShoppingCartDTO.builder()
                .id(shoppingCart.getId())
                .items(shoppingCart.getItems().stream()
                        .map(CartItemMapper::toDTO)
                        .collect(Collectors.toList()))
                .user(userMapper.toDTOCart(shoppingCart.getUser()))
                .totalPrice(shoppingCart.getTotalPrice())
                .build();
    }

    public ShoppingCart toEntity(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartDTO.getId());
        shoppingCart.setItems(shoppingCartDTO.getItems().stream()
                .map(CartItemMapper::toEntity)
                .collect(Collectors.toList()));
        shoppingCart.setUser(userMapper.toEntity(shoppingCartDTO.getUser()));
        return shoppingCart;
    }
}
