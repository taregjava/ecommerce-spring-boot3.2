package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.CategoryDTO;
import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.mapper.CategoryMapper;
import com.halfacode.ecommMaster.mapper.ProductMapper;
import com.halfacode.ecommMaster.models.*;
import com.halfacode.ecommMaster.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private  LocationRepository locationRepository;
    @Autowired
    private ProductInventoryRepository productInventoryRepository;
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

  /*  public ProductDTO addProduct(ProductDTO productDTO, List<MultipartFile> images) throws IOException {
        Product product = ProductMapper.toEntity(productDTO);

        // Handle image uploads
        if (images != null && !images.isEmpty()) {
            List<Image> imageList = images.stream()
                    .map(file -> {
                        try {
                            String imageUrl = cloudinaryService.uploadImage(file);
                            String imageName = cloudinaryService.getImageName(file);
                            Image image = new Image();
                            image.setUrl(imageUrl);
                            image.setName(imageName);
                            image.setProduct(product);
                            return image;
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to upload image: " + file.getOriginalFilename(), e);
                        }
                    })
                    .collect(Collectors.toList());
            product.setImages(imageList);
        }

        return ProductMapper.toDTO(productRepository.save(product));
    }*/
 /* public ProductDTO addProduct(ProductDTO productDTO) {
      // Fetch the category from the database using the category name
      if (productDTO.getCategory() == null || productDTO.getCategory().getName() == null) {
          throw new IllegalArgumentException("Category is required.");
      }

      String categoryName = productDTO.getCategory().getName();
      Category category = (Category) categoryRepository.findByName(categoryName)
              .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));

      // Map DTO to entity
      Product product = ProductMapper.toEntity(productDTO);
      product.setCategory(category); // Associate the existing category

      // Initialize empty images list for the product
      product.setImages(new ArrayList<>());

      // Save the product
      Product savedProduct = productRepository.save(product);

      // Return the saved product as DTO
      return ProductMapper.toDTO(savedProduct);
  }*/

    public ProductDTO addProduct(ProductDTO productDTO) {
        // Fetch category
        if (productDTO.getCategory() == null || productDTO.getCategory().getName() == null) {
            throw new IllegalArgumentException("Category is required.");
        }

        String categoryName = productDTO.getCategory().getName();
        Category category = (Category) categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));

        // Map DTO to entity
        Product product = ProductMapper.toEntity(productDTO);
        product.setCategory(category); // Associate the existing category
        product.setImages(new ArrayList<>()); // Initialize images
        product.setStockQuantity(0); // Set initial total stock as 0 (to be updated via inventory)
        product.setIsAvailable(false); // Product is unavailable until stock is added

        // Save product
        Product savedProduct = productRepository.save(product);

        // Update inventory for specific locations (if provided in DTO)
        if (productDTO.getStockQuantity() != null && productDTO.getStockQuantity() > 0) {
            updateInventoryForLocations(savedProduct, productDTO.getStockQuantity());
        }

        // Return saved product as DTO
        return ProductMapper.toDTO(savedProduct);
    }
    public ProductDTO updateProduct(Long id, ProductDTO updatedProductDTO, List<MultipartFile> images) throws IOException {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProductDTO.getName());
                    product.setDescription(updatedProductDTO.getDescription());
                    product.setPrice(updatedProductDTO.getPrice());
                    product.setStockQuantity(updatedProductDTO.getStockQuantity());
                    product.setIsAvailable(updatedProductDTO.getIsAvailable());

                    // Update category
                    Category category = CategoryMapper.toEntity(updatedProductDTO.getCategory());
                    product.setCategory(category);

                    // Handle image uploads
                    if (images != null && !images.isEmpty()) {
                        List<Image> newImages = images.stream()
                                .map(file -> {
                                    try {
                                        String imageUrl = cloudinaryService.uploadImage(file);
                                        String imageName = cloudinaryService.getImageName(file);
                                        Image image = new Image();
                                        image.setUrl(imageUrl);
                                        image.setName(imageName);
                                        image.setProduct(product);
                                        return image;
                                    } catch (IOException e) {
                                        throw new RuntimeException("Failed to upload image: " + file.getOriginalFilename(), e);
                                    }
                                })
                                .collect(Collectors.toList());
                        product.getImages().addAll(newImages);
                    }

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
        User user = null;
        try {
            user = getCurrentUser();
        } catch (RuntimeException e) {
            // User is not logged in, continue logging without a user
        }

        UserActivity activity = new UserActivity();
        activity.setUser(user); // Can be null for guests
        activity.setProduct(productRepository.findById(productId).orElse(null));
        activity.setViewTime(LocalDateTime.now());
        activity.setAction(action);
        userActivityRepository.save(activity);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null; // No authenticated user
        }

        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        }

        return null;
    }

    public void addImagesToProduct(Long productId, List<MultipartFile> images) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        List<Image> imageList = images.stream()
                .map(file -> {
                    try {
                        String imageUrl = cloudinaryService.uploadImage(file);
                        Image image = new Image();
                        image.setUrl(imageUrl);
                        image.setName(file.getOriginalFilename());
                        image.setProduct(product);
                        return image;
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload image: " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());

        product.getImages().addAll(imageList);
        productRepository.save(product);
    }
    private void updateInventoryForLocations(Product product, Integer totalStock) {
        // Example: Distribute stock equally across locations
        List<Location> locations = locationRepository.findAll(); // Fetch all available locations
        int stockPerLocation = totalStock / locations.size();

        for (Location location : locations) {
            ProductInventory inventory = ProductInventory.builder()
                    .product(product)
                    .location(location)
                    .stockQuantity(stockPerLocation)
                    .isAvailable(stockPerLocation > 0)
                    .build();

            productInventoryRepository.save(inventory);
        }

        // Update total stock in the product entity
        product.setStockQuantity(totalStock);
        product.setIsAvailable(totalStock > 0);
        productRepository.save(product);
    }
  /*  private void updateInventoryForLocations(Product product, Map<String, Integer> locationStock) {
        locationStock.forEach((locationName, stockQuantity) -> {
            Location location = locationRepository.findByName(locationName)
                    .orElseThrow(() -> new RuntimeException("Location not found: " + locationName));

            ProductInventory inventory = ProductInventory.builder()
                    .product(product)
                    .location(location)
                    .stockQuantity(stockQuantity)
                    .isAvailable(stockQuantity > 0)
                    .build();

            productInventoryRepository.save(inventory);
        });

        // Update total stock in the product entity
        int totalStock = locationStock.values().stream().mapToInt(Integer::intValue).sum();
        product.setStockQuantity(totalStock);
        product.setIsAvailable(totalStock > 0);
        productRepository.save(product);
    }*/

}
