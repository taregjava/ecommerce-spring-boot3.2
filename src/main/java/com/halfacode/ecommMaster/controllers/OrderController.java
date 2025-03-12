package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.OrderDTO;
import com.halfacode.ecommMaster.dto.PlaceOrderRequest;
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
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderRequest request) {
        try {
            // Validate if userId exists
            if (request.getUserId() == null) {
                return ResponseEntity.badRequest().body("User ID is required.");
            }

            Long userId = request.getUserId();
            String discountCode = request.getDiscountCode();
            Long addressId = request.getAddressId(); // May be null

            User user = userService.getUserById(userId);

            // Fetch shopping cart for the user
            ShoppingCart shoppingCart = shoppingCartService.getCartByUser(user);
            List<CartItem> cartItems = shoppingCart.getItems();

            if (cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body("Cart is empty.");
            }

            // Place the order (handling null addressId in the service)
            OrderDTO order = orderService.placeOrder(cartItems, discountCode, user, addressId);

            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (CustomPaymentException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
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
