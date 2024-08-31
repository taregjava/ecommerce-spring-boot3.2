package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.Promotion;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionUsageService promotionUsageService;

    /**
     * Applies a promotion to a product based on the promotion code and user's tier.
     * @param code the promotion code
     * @param product the product to which the promotion is applied
     * @param user the user applying the promotion
     * @return the discounted price if the promotion is valid; otherwise, the original price
     */
    public double applyPromotion(String code, Product product, User user) {
        Promotion promotion = promotionRepository.findByCodeAndActiveTrue(code);

        if (promotion != null && promotion.isActive(LocalDateTime.now()) && promotion.getApplicableProducts().contains(product)) {
            // Check if the user's tier is eligible for the promotion
            String userTier = user.getTier(); // Assuming User entity has a 'tier' field
            if (promotion.getApplicableTiers().isEmpty() || promotion.getApplicableTiers().contains(userTier)) {
                double discountedPrice = product.getPrice() * (1 - promotion.getDiscountPercentage() / 100);

                // Log promotion usage
                promotionUsageService.logPromotionUsage(promotion, user, product.getPrice() - discountedPrice);

                return discountedPrice;
            }
        }
        return product.getPrice();
    }


    /**
     * Finds a promotion by its code.
     * @param promoCode the promotion code
     * @return the Promotion if found, otherwise null
     */
    public Promotion findByCode(String promoCode) {
        return promotionRepository.findByCode(promoCode);
    }
}
