package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.ShoppingCartDTO;
import com.halfacode.ecommMaster.mapper.ShoppingCartMapper;
import com.halfacode.ecommMaster.models.ShoppingCart;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.services.ShoppingCartService;
import com.halfacode.ecommMaster.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    private final ShoppingCartMapper shoppingCartMapper;
    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService, ShoppingCartMapper shoppingCartMapper) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.shoppingCartMapper = shoppingCartMapper;
    }

    // Endpoint to add a product to the shopping cart
    @PostMapping("/addcart")
    public ResponseEntity<ShoppingCartDTO> addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        User user = userService.getUserById(userId);  // Fetch the user by ID
        ShoppingCart cart = shoppingCartService.addToCart(user, productId, quantity);  // Add product to cart
        ShoppingCartDTO cartDTO = shoppingCartMapper.toDTO(cart);  // Map to DTO
        return ResponseEntity.ok(cartDTO);  // Return the updated cart DTO
    }



    // Additional endpoints for removing items, clearing the cart, etc., can be added here
}
