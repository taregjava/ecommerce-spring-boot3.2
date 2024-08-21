package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.models.UserActivity;
import com.halfacode.ecommMaster.repositories.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private UserActivityRepository activityRepository;

    public List<Product> getRecommendations(User user) {
        List<Product> recommendedProducts = new ArrayList<>();

        // Example: Recommend products based on the user's viewing history
        List<UserActivity> activities = activityRepository.findByUserAndAction(user, "VIEW");
        Map<Product, Long> frequencyMap = activities.stream()
                .collect(Collectors.groupingBy(UserActivity::getProduct, Collectors.counting()));

        frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed())
                .limit(5) // Recommend top 5
                .forEach(entry -> recommendedProducts.add(entry.getKey()));

        return recommendedProducts;
    }
  /*  public List<Product> recommendProducts(User user) {
        // Example: Recommend products based on the user's past purchases
        List<Product> purchasedProducts = purchaseHistoryRepository.findByUser(user);

        // Implement a basic collaborative filtering algorithm
        return findSimilarProducts(purchasedProducts);
    }*/

   /* private List<Product> findSimilarProducts(List<Product> products) {
        // Placeholder for actual recommendation logic
        // In reality, this would involve more complex ML algorithms
        return productRepository.findTopRatedProducts();
    }*/
}
