package com.halfacode.ecommMaster.errors;

public class CustomPaymentException extends RuntimeException {
    public CustomPaymentException(String message) {
        super(message);
    }

    public CustomPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
