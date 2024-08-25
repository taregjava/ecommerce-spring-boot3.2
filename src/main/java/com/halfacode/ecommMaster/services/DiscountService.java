package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Discount;
import com.halfacode.ecommMaster.repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;
    public Discount getDiscountByCode(String discountCode) {
        // Assuming you fetch the discount from the database
        Discount discount = discountRepository.findByCode(discountCode);
        if (discount == null) {
            // Handle the case where no discount is found
            throw new IllegalArgumentException("Invalid discount code");
        }
        return discount;
    }
}
