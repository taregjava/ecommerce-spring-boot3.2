package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.errors.OrderNotFoundException;
import com.halfacode.ecommMaster.models.Order;
import com.halfacode.ecommMaster.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderTrackingService {

    @Autowired
    private OrderRepository orderRepository;

    public String updateOrderStatus(Long orderId, String status, String trackingNumber) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("order not found with id " +orderId));
        order.setStatus(status);
        order.setTrackingNumber(trackingNumber);
        orderRepository.save(order);
        return "Order status updated to " + status + " with tracking number " + trackingNumber;
    }

    public String getOrderTrackingInfo(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(" null"+orderId));
        return "Order Status: " + order.getStatus() + ", Tracking Number: " + order.getTrackingNumber();
    }
}
