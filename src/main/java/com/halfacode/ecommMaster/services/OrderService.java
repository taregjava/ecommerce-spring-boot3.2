package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.CartItem;
import com.halfacode.ecommMaster.models.Discount;
import com.halfacode.ecommMaster.models.Order;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Order placeOrder(List<CartItem> cartItems, String discountCode, User user) {
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

        // Simulate payment processing
        boolean paymentSuccessful = paymentService.processPayment(user, discountedAmount, "cash");
        if (!paymentSuccessful) {
            throw new IllegalStateException("Payment failed for user: " + user.getUsername());
        }

        // Reduce stock and save order
        for (CartItem item : cartItems) {
            productService.updateStock(item.getProduct().getId(), item.getQuantity());
        }

        // Create and save order
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setItems(cartItems);
        order.setTotalAmount(discountedAmount);
        order.setUser(user);

        return orderRepository.save(order);
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
