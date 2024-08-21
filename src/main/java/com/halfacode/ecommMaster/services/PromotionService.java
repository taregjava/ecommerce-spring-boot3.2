package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.Promotion;
import com.halfacode.ecommMaster.repositories.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public double applyPromotion(String code, Product product) {
        Promotion promotion = promotionRepository.findByCodeAndActiveTrue(code);
        if (promotion != null && promotion.getApplicableProducts().contains(product)) {
            return product.getPrice() * (1 - promotion.getDiscountPercentage() / 100);
        }
        return product.getPrice();
    }
}
