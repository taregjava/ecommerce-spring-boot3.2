package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Discount;
import com.halfacode.ecommMaster.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;
    public Discount getDiscountByCode(String code) {
        return discountRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid discount code: " + code));
    }

}
