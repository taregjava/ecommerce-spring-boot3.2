package com.halfacode.ecommMaster.dto;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class PaymentDTO {
    private String paymentId;
    private String paymentMethod;
    private String paymentStatus;
    private double totalAmount;
    private String currency;
}