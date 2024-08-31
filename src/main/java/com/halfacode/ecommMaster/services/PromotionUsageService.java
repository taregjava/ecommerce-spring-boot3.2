package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Promotion;
import com.halfacode.ecommMaster.models.PromotionUsage;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.PromotionUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PromotionUsageService {

    @Autowired
    private PromotionUsageRepository promotionUsageRepository;

    public void logPromotionUsage(Promotion promotion, User user, double discountApplied) {
        PromotionUsage usage = new PromotionUsage();
        usage.setPromotion(promotion);
        usage.setUser(user);
        usage.setUsageDate(LocalDateTime.now());
        usage.setDiscountApplied(discountApplied);

        promotionUsageRepository.save(usage);
    }
}
