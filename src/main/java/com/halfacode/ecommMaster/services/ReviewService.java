package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.Review;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductService productService;
    public Review addReview(Product product, String reviewText, int rating, User user) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(reviewText);
        review.setReviewDate(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByProduct(Long productId) {
        ProductDTO product = productService.getProductById(productId);
        return reviewRepository.findByProduct(product);
    }
}