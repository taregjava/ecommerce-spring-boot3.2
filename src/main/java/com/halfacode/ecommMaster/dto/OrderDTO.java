package com.halfacode.ecommMaster.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private AddressDTO shippingAddress;
    private AddressDTO billingAddress;
    private String username;  // Add this field to represent the user's username
    private List<OrderItemDTO> items;
    private LocalDateTime deliveryDate;
    private PaymentDTO payment;
    private ShippingDTO shipping;
    private String currency;
    private String trackingNumber;
}
