package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.AddressDTO;
import com.halfacode.ecommMaster.dto.OrderDTO;
import com.halfacode.ecommMaster.dto.PaymentDTO;
import com.halfacode.ecommMaster.dto.ShippingDTO;
import com.halfacode.ecommMaster.models.Address;
import com.halfacode.ecommMaster.models.Order;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
        // Private constructor to prevent instantiation
    }

    public static OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        AddressDTO shippingAddress = getShippingAddress(order);
        AddressDTO billingAddress = getBillingAddress(order);
        PaymentDTO payment = getPaymentDetails(order);
        ShippingDTO shipping = getShippingDetails(order);

        return OrderDTO.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .shippingAddress(shippingAddress)
                .billingAddress(billingAddress)
                .username(order.getUser().getUsername())
                .items(order.getItems().stream()
                        .map(CartItemMapper::toOrderItemDTO)
                        .collect(Collectors.toList()))
                .payment(payment)
                .shipping(shipping)
                .deliveryDate(order.getDeliveryDate())
                .currency("USD")
                .trackingNumber(order.getTrackingNumber())
                .paymentUrl(order.getPaymentUrl())
                .build();
    }

    private static AddressDTO getShippingAddress(Order order) {
        Address address = order.getShippingAddress();
        if (address == null) return null;

        return AddressDTO.builder()
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }

    private static AddressDTO getBillingAddress(Order order) {
        Address address = order.getShippingAddress();
        if (address == null) return null;

        return AddressDTO.builder()
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }

    private static PaymentDTO getPaymentDetails(Order order) {
        if (order.getPaymentId() == null || order.getPaymentMethod() == null) {
            return null;
        }

        return PaymentDTO.builder()
                .paymentId(order.getPaymentId())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus("Paid")
                .totalAmount(order.getTotalAmount())
                .currency("USD")
                .build();
    }

    private static ShippingDTO getShippingDetails(Order order) {
        if (order.getShippingAddress() == null) {
            return null;
        }

        return ShippingDTO.builder()
                .shippingMethod("Standard")
                .shippingCost(5.99)
                .estimatedDelivery(order.getDeliveryDate() != null ? order.getDeliveryDate().toString() : "Unknown")
                .build();
    }
}
