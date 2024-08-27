package com.halfacode.ecommMaster.mapper;



import com.halfacode.ecommMaster.dto.AddressDTO;
import com.halfacode.ecommMaster.dto.OrderDTO;
import com.halfacode.ecommMaster.dto.PaymentDTO;
import com.halfacode.ecommMaster.dto.ShippingDTO;
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
        // Assume there are services or methods to get these details
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
                .username(order.getUser().getUsername())  // Map username directly
                .items(order.getItems().stream()
                        .map(CartItemMapper::toOrderItemDTO)  // Use new method in CartItemMapper
                        .collect(Collectors.toList()))
                .payment(payment)
                .shipping(shipping)
                .deliveryDate(order.getDeliveryDate())
                .currency("USD")
                .trackingNumber(order.getTrackingNumber())
                .build();
    }

    private static AddressDTO getShippingAddress(Order order) {
        // Implement logic to fetch shipping address from order
        return AddressDTO.builder()
                .addressLine1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("USA")
                .build();
    }
    private static AddressDTO getBillingAddress(Order order) {
        // Implement logic to fetch billing address from order
        return AddressDTO.builder()
                .addressLine1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("USA")
                .build();
    }

    private static PaymentDTO getPaymentDetails(Order order) {
        // Implement logic to fetch payment details
        return PaymentDTO.builder()
                .paymentId("111213")
                .paymentMethod("Credit Card")
                .paymentStatus("Paid")
                .totalAmount(order.getTotalAmount())
                .currency("USD")
                .build();
    }

    private static ShippingDTO getShippingDetails(Order order) {
        // Implement logic to fetch shipping details
        return ShippingDTO.builder()
                .shippingMethod("Standard")
                .shippingCost(5.99)
                .estimatedDelivery("2024-09-01")
                .build();
    }
}
