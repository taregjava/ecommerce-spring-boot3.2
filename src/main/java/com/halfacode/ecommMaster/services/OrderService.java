package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.OrderDTO;
import com.halfacode.ecommMaster.errors.CustomPaymentException;
import com.halfacode.ecommMaster.mapper.OrderMapper;
import com.halfacode.ecommMaster.models.CartItem;
import com.halfacode.ecommMaster.models.Discount;
import com.halfacode.ecommMaster.models.Order;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final DiscountService discountService;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService,
                        PaymentService paymentService, DiscountService discountService,
                        ShoppingCartService shoppingCartService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.paymentService = paymentService;
        this.discountService = discountService;
        this.shoppingCartService = shoppingCartService;
    }

    @Transactional
    public OrderDTO placeOrder(List<CartItem> cartItems, String discountCode, User user) {
        try {
            // Validate and apply discount
            double discountPercentage = 0;
            if (discountCode != null && !discountCode.isEmpty()) {
                Discount discount = discountService.getDiscountByCode(discountCode);
                if (discount != null && discount.isActive()) {
                    discountPercentage = discount.getPercentage();
                } else {
                    throw new IllegalArgumentException("Invalid or expired discount code");
                }
            }

            // Calculate total amount with discount
            double totalAmount = cartItems.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();
            double discountedAmount = totalAmount * (1 - discountPercentage / 100);

            // Log the amounts
            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Discounted Amount: " + discountedAmount);

            // Simulate payment processing
            boolean paymentSuccessful = paymentService.processPayment(user, discountedAmount, "CREDIT_CARD");
            if (!paymentSuccessful) {
                throw new CustomPaymentException("Payment failed for user: " + user.getUsername());
            }

            // Reduce stock
            for (CartItem item : cartItems) {
                productService.updateStock(item.getProduct().getId(), item.getQuantity());
                // Log stock update
                System.out.println("Updated stock for product ID: " + item.getProduct().getId());
            }

            // Create and save order
            Order order = new Order();
            order.setOrderDate(LocalDateTime.now());
            order.setItems(new ArrayList<>(cartItems)); // Create a new list to avoid shared references
            order.setTotalAmount(discountedAmount);
            order.setUser(user);
            Order savedOrder = orderRepository.save(order);

            // Log order details
            System.out.println("Saved Order ID: " + savedOrder.getId());

            // Clear cart after order is placed
            shoppingCartService.clearCart(user);
            System.out.println("Cart cleared for user: " + user.getUsername());

            return OrderMapper.toDTO(savedOrder);

        } catch (IllegalArgumentException e) {
            // Handle invalid discount code
            System.err.println("Invalid discount code: " + e.getMessage());
            throw new RuntimeException("Order placement failed due to invalid discount: " + e.getMessage(), e);
        } catch (CustomPaymentException e) {
            // Handle payment failure specifically
            System.err.println("Payment error: " + e.getMessage());
            throw new CustomPaymentException("Order placement failed due to payment failure: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle any other unforeseen errors
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while placing the order: " + e.getMessage(), e);
        }
    }





    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.updateStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}
