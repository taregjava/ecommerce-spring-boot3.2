package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.errors.CustomPaymentException;
import com.halfacode.ecommMaster.models.CartItem;
import com.halfacode.ecommMaster.models.Order;
import com.halfacode.ecommMaster.models.ShoppingCart;
import com.halfacode.ecommMaster.models.User;

import com.halfacode.ecommMaster.services.OrderService;
import com.halfacode.ecommMaster.services.ShoppingCartService;
import com.halfacode.ecommMaster.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, ShoppingCartService shoppingCartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
    }

    // Endpoint to place an order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestParam String discountCode, @RequestParam Long userId) {
        try {
            User user = userService.getUserById(userId);
            ShoppingCart shoppingCart = shoppingCartService.getCartByUser(user);
            List<CartItem> cartItems = shoppingCart.getItems();

            if (cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body("Cart is empty.");
            }

            Order order = orderService.placeOrder(cartItems, discountCode, user);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (CustomPaymentException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while placing the order.");
        }
    }


    // Endpoint to update the order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // Endpoint to get orders by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }
}
