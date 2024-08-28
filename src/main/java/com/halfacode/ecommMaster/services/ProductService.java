package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.CategoryDTO;
import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.mapper.CategoryMapper;
import com.halfacode.ecommMaster.mapper.ProductMapper;
import com.halfacode.ecommMaster.models.Category;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.models.UserActivity;
import com.halfacode.ecommMaster.repositories.ProductRepository;
import com.halfacode.ecommMaster.repositories.UserActivityRepository;
import com.halfacode.ecommMaster.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> {
                    ProductDTO dto = ProductMapper.toDTO(product);
                    // Handle null category
                    if (dto.getCategory() == null) {
                        dto.setCategory(new CategoryDTO(0L, "Uncategorized", ""));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        ProductDTO productDTO= productRepository.findById(id)
                .map(ProductMapper::toDTO)
                .orElse(null);

        if (productDTO != null){
            logUserActivity(productDTO.getId(),"VIEW");
        }
        return productDTO;
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = ProductMapper.toEntity(productDTO);
        return ProductMapper.toDTO(productRepository.save(product));
    }

    public ProductDTO updateProduct(Long id, ProductDTO updatedProductDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProductDTO.getName());
                    product.setDescription(updatedProductDTO.getDescription());
                    product.setPrice(updatedProductDTO.getPrice());
                    product.setStockQuantity(updatedProductDTO.getStockQuantity());
                    product.setIsAvailable(updatedProductDTO.getIsAvailable());

                    // Convert CategoryDTO to Category and set it
                    Category category = CategoryMapper.toEntity(updatedProductDTO.getCategory());
                    product.setCategory(category);

                    return ProductMapper.toDTO(productRepository.save(product));
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private void logUserActivity(Long productId, String action) {
        // Retrieve current user from UserService
        User user = getCurrentUser();
        if (user != null) {
            UserActivity activity = new UserActivity();
            activity.setUser(user);
            activity.setProduct(productRepository.findById(productId).orElse(null));
            activity.setViewTime(LocalDateTime.now());
            activity.setAction(action);
            userActivityRepository.save(activity);
        }
    }
    private User getCurrentUser() {
        // Obtain the authentication from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
        throw new RuntimeException("No user is authenticated");
    }

}
