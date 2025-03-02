package com.halfacode.ecommMaster.dto;

public class PlaceOrderRequest {
    private Long userId;
    private String discountCode;

    // Constructors
    public PlaceOrderRequest() {}

    public PlaceOrderRequest(Long userId, String discountCode) {
        this.userId = userId;
        this.discountCode = discountCode;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }
}
