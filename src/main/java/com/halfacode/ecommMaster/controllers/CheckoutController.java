package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.OrderDTO;
import com.halfacode.ecommMaster.models.*;
import com.halfacode.ecommMaster.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CheckoutController {

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private PricingService pricingService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecommendationService recommendationService;

    // Endpoint to add an item to the cart
    @PostMapping("/add")
    public ResponseEntity<ShoppingCart> addToCart(@RequestHeader("Authorization") String authHeader,
                                                  @RequestParam Long productId,
                                                  @RequestParam int quantity) {
        User user = userService.getUserFromAuthHeader(authHeader);
        ShoppingCart cart = cartService.addToCart(user, productId, quantity);
        return ResponseEntity.ok(cart);
    }

    // Endpoint to remove an item from the cart
    @PostMapping("/remove")
    public ResponseEntity<ShoppingCart> removeFromCart(@RequestHeader("Authorization") String authHeader,
                                                       @RequestParam Long productId) {
        User user = userService.getUserFromAuthHeader(authHeader);
        ShoppingCart cart = cartService.removeFromCart(user, productId);
        return ResponseEntity.ok(cart);
    }

    // Endpoint to clear the cart
    @PostMapping("/clear")
    public ResponseEntity<ShoppingCart> clearCart(@RequestHeader("Authorization") String authHeader) {
        User user = userService.getUserFromAuthHeader(authHeader);
        ShoppingCart cart = cartService.clearCart(user);
        return ResponseEntity.ok(cart);
    }

    // Endpoint to checkout the cart
    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(@RequestHeader("Authorization") String authHeader,
                                          @RequestParam("paymentMethod") String paymentMethod) {
        User user = userService.getUserFromAuthHeader(authHeader);
        ShoppingCart cart = cartService.getCartByUser(user);

        // Calculate final price
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> pricingService.calculatePrice(item.getProduct(), user) * item.getQuantity())
                .sum();

        // Place order
        OrderDTO order = orderService.placeOrder(cart.getItems(), paymentMethod, user);

        // Clear cart after successful order
        cartService.clearCart(user);

        // Recommend products to the user based on their current order
        List<Product> recommendations = recommendationService.getRecommendations(user);

        return ResponseEntity.ok(order);
    }
}
