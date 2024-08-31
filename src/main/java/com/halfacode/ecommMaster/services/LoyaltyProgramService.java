package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.models.Promotion;
import com.halfacode.ecommMaster.repositories.ProductRepository;
import com.halfacode.ecommMaster.repositories.UserRepository;
import com.halfacode.ecommMaster.repositories.TierRepository;
import com.halfacode.ecommMaster.repositories.PromotionRepository;
import com.halfacode.ecommMaster.models.Tier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoyaltyProgramService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TierRepository tierRepository;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private PromotionUsageService promotionUsageService;

    @Autowired
    private ProductRepository productRepository;
    /**
     * Adds loyalty points to the user's account based on the amount spent and applicable promotions.
     * @param user the user receiving the points
     * @param amountSpent the amount of money spent
     * @param productCategory the category of the product being purchased
     */
    public void addLoyaltyPoints(User user, double amountSpent, String productCategory, String promoCode) {
        // Get the user's tier
        String userTier = user.getTier();

        // Retrieve Products based on productCategory
        List<Product> products = productRepository.findByCategoryName(productCategory);
        if (products.isEmpty()) {
            throw new IllegalArgumentException("No products found for category: " + productCategory);
        }

        // Calculate points based on tier conversion rate
        int conversionRate = getConversionRate(userTier);
        double points = calculatePoints(amountSpent, conversionRate);

        for (Product product : products) {
            // Check and apply promotion if active
            double discountedAmount = promotionService.applyPromotion(promoCode, product, user);
            if (discountedAmount < amountSpent) {
                // Log promotion usage
                Promotion promotion = promotionService.findByCode(promoCode);
                promotionUsageService.logPromotionUsage(promotion, user, amountSpent - discountedAmount);
            }
        }

        // Add points to the user's account
        user.setLoyaltyPoints(user.getLoyaltyPoints() + (int) points);
        userRepository.save(user);
    }




    /**
     * Gets the conversion rate based on the user's tier.
     * @param tierName the name of the user's tier
     * @return the conversion rate for the tier
     */
    private int getConversionRate(String tierName) {
        return tierRepository.findByName(tierName)
                .orElseThrow(() -> new IllegalArgumentException("Tier not found"))
                .getConversionRate();
    }
    public int redeemLoyaltyPoints(User user, int points) {
        if (user.getLoyaltyPoints() >= points) {
            // Deduct the points
            user.setLoyaltyPoints(user.getLoyaltyPoints() - points);
            userRepository.save(user);
            return points;
        } else {
            // Not enough points to redeem
            throw new IllegalArgumentException("Insufficient loyalty points");
        }
    }



    private double calculatePoints(double amountSpent, int conversionRate) {
        return amountSpent * conversionRate;
    }
}
