package com.halfacode.ecommMaster.dto;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ShippingDTO {
    private String shippingMethod;
    private double shippingCost;
    private String estimatedDelivery;
}