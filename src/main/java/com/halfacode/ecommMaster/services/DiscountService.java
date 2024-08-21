package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Discount;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {
    public Discount getDiscountByCode(String discountCode) {

        return new Discount();
    }
}
