package com.halfacode.ecommMaster.errors;

public class VendorNotFoundException extends RuntimeException{

    public VendorNotFoundException(String msg){
        super(msg);
    }
}
