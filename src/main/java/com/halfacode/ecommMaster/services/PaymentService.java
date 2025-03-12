package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.User;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

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
}
