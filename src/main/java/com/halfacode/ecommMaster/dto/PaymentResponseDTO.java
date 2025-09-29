package com.halfacode.ecommMaster.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {
    private Long orderId;
    private String paymentId;
    private String status;
}

