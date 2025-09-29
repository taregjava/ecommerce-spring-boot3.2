package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.PaymentResponseDTO;
import com.halfacode.ecommMaster.models.Order;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {


    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean processPayment(User user, double amount, String paymentMethod) {
        switch (paymentMethod.toUpperCase()) {
            case "CREDIT_CARD":
                return processCreditCardPayment(user, amount);
            case "PAYPAL":
                return processPaypalPayment(user, amount);
            case "CASH":
                return processCashPayment(user, amount);
            default:
                throw new IllegalArgumentException("Unsupported payment method");
        }
    }

    private boolean processCreditCardPayment(User user, double amount) {
        System.out.println("Processing credit card payment of " + amount + " for user: " + user.getUsername());
        return true; // Simulate successful payment
    }

    private boolean processPaypalPayment(User user, double amount) {
        System.out.println("Processing PayPal payment of " + amount + " for user: " + user.getUsername());
        return true; // Simulate successful payment
    }

    private boolean processCashPayment(User user, double amount) {
        System.out.println("Processing cash payment for " + amount + " at store for user: " + user.getUsername());
        return true; // Assume successful cash payment
    }

    @Transactional
    public ResponseEntity<String> verifyPayment(PaymentResponseDTO paymentResponse) {
        Long orderId = paymentResponse.getOrderId();
        String paymentStatus = paymentResponse.getStatus();
        String paymentId = paymentResponse.getPaymentId(); // Capture paymentId

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("SUCCESS".equalsIgnoreCase(paymentStatus)) {
            order.setStatus("CONFIRMED");
            order.setPaymentId(paymentId);  // Save the payment transaction ID
            orderRepository.save(order);
            return ResponseEntity.ok("Payment successful, order confirmed.");
        } else {
            order.setStatus("FAILED");
            orderRepository.save(order);
            return ResponseEntity.badRequest().body("Payment failed.");
        }
    }

}
