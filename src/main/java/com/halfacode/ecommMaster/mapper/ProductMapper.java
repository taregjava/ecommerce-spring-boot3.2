package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.CategoryDTO;
import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.dto.ReviewDTO;
import com.halfacode.ecommMaster.dto.UserDto;
import com.halfacode.ecommMaster.models.Category;
import com.halfacode.ecommMaster.models.Image;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    private ProductMapper() {
        // Private constructor to prevent instantiation
    }

   /* public static ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        List<String> imageUrls = product.getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        List<ReviewDTO> reviewDTOs = product.getReviews().stream()
                .map(review -> {
                    ReviewDTO reviewDTO = new ReviewDTO();
                    reviewDTO.setId(review.getId());
                    reviewDTO.setRating(review.getRating());
                    reviewDTO.setComment(review.getComment());
                    reviewDTO.setReviewDate(review.getReviewDate());
                  //  reviewDTO.setUserName(review.getUser().getName()); // Assuming User has a `name` field
                    return reviewDTO;
                })
                .collect(Collectors.toList());

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .isAvailable(product.getIsAvailable())
                .averageRating(product.getAverageRating())
                .basePrice(product.getBasePrice())
                .salesCount(product.getSalesCount())
                .category(product.getCategory() != null ?
                        new CategoryDTO(product.getCategory().getId(),
                                product.getCategory().getName(),
                                product.getCategory().getDescription()) : null)
                .imageUrls(imageUrls)
                .reviews(reviewDTOs) // Map reviews here
                .build();
    }
*/
   public static ProductDTO toDTO(Product product) {
       if (product == null) {
           return null;
       }

       List<String> imageUrls = product.getImages().stream()
               .map(Image::getUrl)
               .collect(Collectors.toList());

       List<ReviewDTO> reviewDTOs = product.getReviews().stream()
               .map(ProductMapper::toReviewDTO)
               .collect(Collectors.toList());

       return ProductDTO.builder()
               .id(product.getId())
               .name(product.getName())
               .description(product.getDescription())
               .price(product.getPrice())
               .stockQuantity(product.getStockQuantity())
               .isAvailable(product.getIsAvailable())
               .averageRating(product.getAverageRating())
               .basePrice(product.getBasePrice())
               .salesCount(product.getSalesCount())
               .category(product.getCategory() != null ?
                       new CategoryDTO(product.getCategory().getId(),
                               product.getCategory().getName(),
                               product.getCategory().getDescription()) : null)
               .imageUrls(imageUrls)
               .reviews(reviewDTOs) // Use the mapped reviews
               .build();
   }

    public static Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setIsAvailable(productDTO.getIsAvailable());
        product.setBasePrice(productDTO.getBasePrice());
        product.setSalesCount(productDTO.getSalesCount());

        CategoryDTO categoryDTO = productDTO.getCategory();
        if (categoryDTO != null) {
            Category category = new Category(categoryDTO.getName(), categoryDTO.getDescription());
            product.setCategory(category);
        }
        product.setImages(new ArrayList<>());

        return product;
    }

    public static ProductDTO toCartDTO(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .isAvailable(product.getIsAvailable())
                .averageRating(product.getAverageRating())
                .basePrice(product.getBasePrice())
                .salesCount(product.getSalesCount())
                .category(product.getCategory() != null ?
                        new CategoryDTO(product.getCategory().getName(), product.getCategory().getDescription()) : null)
               // .imageUrls(imageUrls)
                .build();
    }

    public static ReviewDTO toReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setReviewDate(review.getReviewDate());

        // Convert User entity to UserDto
        if (review.getUser() != null) {
            UserDto userDto = UserDto.builder()
                    .id(review.getUser().getId())
                    .username(review.getUser().getUsername()) // Ensure UserDto has `username`
                    .build();

            reviewDTO.setUser(userDto);
        } else {
            reviewDTO.setUser(null);  // or provide a default UserDto
        }

        return reviewDTO;
    }


}
