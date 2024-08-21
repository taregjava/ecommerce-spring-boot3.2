package com.halfacode.ecommMaster.errors;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(String error){
        super(error);
    }
}
