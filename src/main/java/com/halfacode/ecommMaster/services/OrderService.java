package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.OrderDTO;
import com.halfacode.ecommMaster.errors.CustomPaymentException;
import com.halfacode.ecommMaster.mapper.OrderMapper;
import com.halfacode.ecommMaster.models.*;
import com.halfacode.ecommMaster.repositories.AddressRepository;
import com.halfacode.ecommMaster.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PaymentService paymentService;
    private final DiscountService discountService;
    private final ShoppingCartService shoppingCartService;
    private final AddressRepository addressRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService,
                        PaymentService paymentService, DiscountService discountService,
                        ShoppingCartService shoppingCartService, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.paymentService = paymentService;
        this.discountService = discountService;
        this.shoppingCartService = shoppingCartService;
        this.addressRepository = addressRepository;
    }
    @Transactional
    public OrderDTO placeOrder(List<CartItem> cartItems, String discountCode, User user, Long addressId) {
        Address shippingAddress = (addressId != null)
                ? addressRepository.findById(addressId).orElse(null)
                : addressRepository.findByUserAndDefaultAddressTrue(user);

        if (shippingAddress == null) {
            throw new RuntimeException("Shipping address is required");
        }

        double discountPercentage = 0;
        if (discountCode != null && !discountCode.isEmpty()) {
            Discount discount = discountService.getDiscountByCode(discountCode);
            if (discount != null && discount.isActive()) {
                discountPercentage = discount.getPercentage();
            } else {
                throw new IllegalArgumentException("Invalid or expired discount code");
            }
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        double discountedAmount = totalAmount * (1 - discountPercentage / 100);

        boolean paymentSuccessful = paymentService.processPayment(user, discountedAmount, "CREDIT_CARD");
        if (!paymentSuccessful) {
            throw new CustomPaymentException("Payment failed for user: " + user.getUsername());
        }

        for (CartItem item : cartItems) {
            productService.updateStock(item.getProduct().getId(), item.getQuantity());
        }

        // Step 1: Create the order with "PENDING_PAYMENT" status
        Order order = new Order();
        List<CartItem> orderItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            CartItem newItem = new CartItem(item.getProduct(), item.getQuantity());
            newItem.setTotalPrice(item.getTotalPrice());
            newItem.setOrder(order);
            orderItems.add(newItem);
        }
        order.setItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(discountedAmount);
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus("PENDING_PAYMENT");
        order.setTrackingNumber(UUID.randomUUID().toString());
        order.setDeliveryDate(LocalDateTime.now().plusDays(5));
        order.setPaymentId(null);  // Payment ID will be updated after payment
        Order savedOrder = orderRepository.saveAndFlush(order);

        // Step 2: Generate and set the payment URL
        String paymentUrl = generatePaymentUrl(savedOrder.getId());
        savedOrder.setPaymentUrl(paymentUrl);
        orderRepository.save(savedOrder); // Persist the updated order with payment URL

        // Step 3: Return OrderDTO with Payment URL
        OrderDTO orderDTO = OrderMapper.toDTO(savedOrder);
        orderDTO.setPaymentUrl(paymentUrl);

        return orderDTO;
    }

 /*   @Transactional
    public OrderDTO placeOrder(List<CartItem> cartItems, String discountCode, User user, Long addressId) {
        // If addressId is null, use the default address of the user
        Address shippingAddress = (addressId != null)
                ? addressRepository.findById(addressId).orElse(null)
                : addressRepository.findByUserAndDefaultAddressTrue(user);

        if (shippingAddress == null) {
            throw new RuntimeException("Shipping address is required");
        }

        double discountPercentage = 0;
        if (discountCode != null && !discountCode.isEmpty()) {
            Discount discount = discountService.getDiscountByCode(discountCode);
            if (discount != null && discount.isActive()) {
                discountPercentage = discount.getPercentage();
            } else {
                throw new IllegalArgumentException("Invalid or expired discount code");
            }
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        double discountedAmount = totalAmount * (1 - discountPercentage / 100);

        boolean paymentSuccessful = paymentService.processPayment(user, discountedAmount, "CREDIT_CARD");
        if (!paymentSuccessful) {
            throw new CustomPaymentException("Payment failed for user: " + user.getUsername());
        }

        for (CartItem item : cartItems) {
            productService.updateStock(item.getProduct().getId(), item.getQuantity());
        }

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        List<CartItem> orderItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            CartItem newItem = new CartItem(item.getProduct(), item.getQuantity());
            newItem.setTotalPrice(item.getTotalPrice());
            newItem.setOrder(order);
            orderItems.add(newItem);
        }
        order.setItems(orderItems);


        order.setTotalAmount(discountedAmount);
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus("PENDING_PAYMENT");

        order.setTrackingNumber(UUID.randomUUID().toString());
        order.setDeliveryDate(LocalDateTime.now().plusDays(5));

        Order savedOrder = orderRepository.saveAndFlush(order);

        shoppingCartService.clearCart(user);
 // Step 2: Generate a payment URL (Simulated)
        String paymentUrl = "https://payments.gateway.com/pay?orderId=" + savedOrder.getId();

        OrderDTO orderDTO = OrderMapper.toDTO(savedOrder);
        orderDTO.setPaymentUrl(paymentUrl);
        return orderDTO;
    }*/

    @Transactional
    public OrderDTO placeOrderCart(List<CartItem> cartItems, String discountCode, String paymentMethod, User user) {
        try {
            double discountPercentage = 0;

            // Validate and apply discount if provided
            if (discountCode != null && !discountCode.isEmpty()) {
                Discount discount = discountService.getDiscountByCode(discountCode);
                if (discount != null && discount.isActive()) {
                    discountPercentage = discount.getPercentage();
                } else {
                    throw new IllegalArgumentException("Invalid or expired discount code");
                }
            }

            // Calculate total amount with or without discount
            double totalAmount = cartItems.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();
            double discountedAmount = totalAmount * (1 - discountPercentage / 100);

            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Discounted Amount: " + discountedAmount);

            // Process payment
            boolean paymentSuccessful = paymentService.processPayment(user, discountedAmount, paymentMethod);
            if (!paymentSuccessful) {
                throw new CustomPaymentException("Payment failed for user: " + user.getUsername());
            }

            // Reduce stock
            for (CartItem item : cartItems) {
                productService.updateStock(item.getProduct().getId(), item.getQuantity());
                System.out.println("Updated stock for product ID: " + item.getProduct().getId());
            }

            // Create and save order
            Order order = new Order();
            order.setOrderDate(LocalDateTime.now());
            order.setItems(new ArrayList<>(cartItems));
            order.setTotalAmount(discountedAmount);
            order.setUser(user);
            order.setPaymentMethod(paymentMethod);

            order.setStatus("Pending"); //
            order.setTrackingNumber(UUID.randomUUID().toString()); // Generate tracking number
            order.setDeliveryDate(LocalDateTime.now().plusDays(5)); // Example delivery date

            Order savedOrder = orderRepository.saveAndFlush(order);


            System.out.println("Saved Order ID: " + savedOrder.getId());

            // Clear cart
            shoppingCartService.clearCart(user);
            System.out.println("Cart cleared for user: " + user.getUsername());

            return OrderMapper.toDTO(savedOrder);

        } catch (IllegalArgumentException e) {
            System.err.println("Invalid discount code: " + e.getMessage());
            throw new RuntimeException("Order placement failed due to invalid discount: " + e.getMessage(), e);
        } catch (CustomPaymentException e) {
            System.err.println("Payment error: " + e.getMessage());
            throw new CustomPaymentException("Order placement failed due to payment failure: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while placing the order: " + e.getMessage(), e);
        }
    }



    private String generatePaymentUrl(Long orderId) {
        return "https://payments.gateway.com/pay?orderId=" + orderId;
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
