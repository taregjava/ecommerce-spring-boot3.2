package com.halfacode.ecommMaster.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private AddressDTO shippingAddress;
    private AddressDTO billingAddress;
    private String username;
    private List<OrderItemDTO> items;
    private LocalDate deliveryDate;
    private PaymentDTO payment;
    private ShippingDTO shipping;
    private String currency;
    //private TrackingDTO tracking;
}

