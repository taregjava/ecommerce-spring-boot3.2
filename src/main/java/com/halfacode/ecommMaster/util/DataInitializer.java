package com.halfacode.ecommMaster.util;

import com.halfacode.ecommMaster.models.*;
import com.halfacode.ecommMaster.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RoleRepository roleRepository; // Add RoleRepository
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner initializeData() {
        return args -> {

            List<Role> roles = List.of(
                    new Role("ROLE_USER"),
                    new Role("ROLE_ADMIN")
            );
            if (roleRepository.count() == 0) {
                roleRepository.saveAll(roles);
            }
            // Save roles to the database
            roleRepository.saveAll(roles);

            // Create users and assign roles
            User adminUser = new User();
            adminUser.setUsername("admin");
           // adminUser.setPassword("admin123");  // In a real app, hash the password
            adminUser.setEnabled(true);
            adminUser.setAccountNonLocked(true);
            adminUser.setRoles(new HashSet<>(Set.of(roles.get(1)))); // Assign ROLE_ADMIN
            adminUser.setPassword(passwordEncoder.encode("admin123"));

            User regularUser = new User();
            regularUser.setUsername("user");
            //regularUser.setPassword("user123");  // In a real app, hash the password
            regularUser.setEnabled(true);
            regularUser.setAccountNonLocked(true);
            regularUser.setRoles(new HashSet<>(Set.of(roles.get(0)))); // Assign ROLE_USER
            regularUser.setPassword(passwordEncoder.encode("user123"));

            if (userRepository.count() == 0) {
                userRepository.save(adminUser);
                userRepository.save(regularUser);
            }
// Initialize discounts
            Discount discount1 = new Discount();
            discount1.setCode("SUMMER2024");
            discount1.setPercentage(10.0);
            discount1.setStartDate(LocalDate.of(2024, 6, 1));
            discount1.setEndDate(LocalDate.of(2024, 8, 31));

            Discount discount2 = new Discount();
            discount2.setCode("WINTER2024");
            discount2.setPercentage(15.0);
            discount2.setStartDate(LocalDate.of(2024, 12, 1));
            discount2.setEndDate(LocalDate.of(2025, 2, 28));

            if (discountRepository.count() == 0) {
                discountRepository.save(discount1);
                discountRepository.save(discount2);
            }
            // Define categories
            List<Category> categories = List.of(
                    new Category("Electronics", "Devices and gadgets"),
                    new Category("Books", "Various kinds of books"),
                    new Category("Clothing", "Apparel and accessories"),
                    new Category("Home Appliances", "Appliances for home use"),
                    new Category("Sports", "Sports equipment and gear"),
                    new Category("Toys", "Children's toys and games"),
                    new Category("Furniture", "Furniture for home and office"),
                    new Category("Beauty", "Beauty and personal care products"),
                    new Category("Health", "Health and wellness products"),
                    new Category("Automotive", "Automotive parts and accessories"),
                    new Category("Jewelry", "Jewelry and watches"),
                    new Category("Garden", "Gardening tools and supplies"),
                    new Category("Pet Supplies", "Products for pets"),
                    new Category("Music", "Musical instruments and accessories"),
                    new Category("Office Supplies", "Supplies for office use"),
                    new Category("Food & Beverages", "Groceries and beverages"),
                    new Category("Baby Products", "Products for infants and toddlers"),
                    new Category("Crafts", "Crafting supplies and materials"),
                    new Category("Health & Personal Care", "Products for personal health and care"),
                    new Category("Kitchen", "Kitchenware and gadgets"),
                    new Category("Outdoor", "Outdoor gear and equipment"),
                    new Category("Travel", "Travel accessories and bags"),
                    new Category("Software", "Software and applications"),
                    new Category("Hardware", "Computer hardware and accessories"),
                    new Category("Books & Media", "Books, movies, and music"),
                    new Category("Luggage", "Luggage and travel bags"),
                    new Category("Office Furniture", "Furniture for office spaces"),
                    new Category("Gaming", "Video games and gaming accessories"),
                    new Category("Specialty Foods", "Gourmet and specialty foods"),
                    new Category("Vintage", "Vintage and collectible items"),
                    new Category("Educational", "Educational materials and tools"),
                    new Category("Automotive Tools", "Tools and accessories for automotive repair"),
                    new Category("Camping", "Camping gear and supplies"),
                    new Category("Weddings", "Wedding supplies and accessories"),
                    new Category("Party Supplies", "Supplies for parties and events"),
                    new Category("DIY", "Do-it-yourself tools and kits"),
                    new Category("Photography", "Photography equipment and accessories"),
                    new Category("Fitness", "Fitness equipment and apparel"),
                    new Category("Hobbies", "Hobby supplies and materials"),
                    new Category("Accessories", "Fashion and other accessories"),
                    new Category("Cleaning", "Cleaning products and tools"),
                    new Category("Seasonal", "Seasonal decorations and items"),
                    new Category("Arts & Crafts", "Art supplies and craft materials"),
                    new Category("Technology", "Technology products and gadgets"),
                    new Category("Home Decor", "Decorative items for home"),
                    new Category("Personal Care", "Personal grooming and care products"),
                    new Category("Office Equipment", "Office equipment and gadgets"),
                    new Category("Luxury", "Luxury goods and high-end products"),
                    new Category("Collectibles", "Collectible items and antiques"),
                    new Category("Craft Beer", "Craft beers and brewing supplies"),
                    new Category("Fine Art", "Fine art and paintings"),
                    new Category("Handmade", "Handmade and artisanal products"),
                    new Category("Stationery", "Stationery and writing materials"),
                    new Category("Household", "Household items and products"),
                    new Category("Subscription Boxes", "Monthly subscription boxes"),
                    new Category("Subscription Services", "Subscription services and memberships"),
                    new Category("Smart Home", "Smart home devices and accessories"),
                    new Category("Wireless", "Wireless technology and gadgets"),
                    new Category("Sustainable", "Sustainable and eco-friendly products"),
                    new Category("Tech Gadgets", "Latest technology gadgets"),
                    new Category("Cameras", "Cameras and imaging equipment"),
                    new Category("Bedding", "Bedding and bedroom accessories"),
                    new Category("Furniture & Decor", "Furniture and home decor"),
                    new Category("Office Supplies & Furniture", "Office supplies and furniture"),
                    new Category("Gaming Accessories", "Accessories for gaming"),
                    new Category("DIY Kits", "Do-it-yourself kits and materials"),
                    new Category("Travel Gear", "Gear and accessories for travel"),
                    new Category("Sewing", "Sewing machines and supplies"),
                    new Category("Wine & Spirits", "Wine and spirits collection"),
                    new Category("Gourmet Foods", "Gourmet and specialty foods"),
                    new Category("Gift Ideas", "Ideas and products for gifts"),
                    new Category("Luxury Accessories", "High-end luxury accessories"),
                    new Category("Healthy Living", "Products for a healthy lifestyle"),
                    new Category("Pet Care", "Care products for pets"),
                    new Category("Holiday Gifts", "Gifts for holidays and special occasions"),
                    new Category("Kids", "Products for children"),
                    new Category("Eco-Friendly", "Eco-friendly and sustainable products")
            );
            if (categoryRepository.count() == 0) {
                categoryRepository.saveAll(categories);
            }
            // Save categories to the database
            categoryRepository.saveAll(categories);

            // Define realistic products and associate them with categories
            List<Product> products = List.of(
                    // Electronics
                    new Product("Smartphone", "Latest model with high-resolution display", 699.99, 100, true, categories.get(0)),
                    new Product("Laptop", "High-performance laptop with 16GB RAM", 1199.99, 50, true, categories.get(0)),
                    new Product("Headphones", "Noise-cancelling over-ear headphones", 199.99, 150, true, categories.get(0)),

                    // Books
                    new Product("The Great Gatsby", "Classic novel by F. Scott Fitzgerald", 10.99, 200, true, categories.get(1)),
                    new Product("Java Programming", "Comprehensive guide to Java programming", 29.99, 100, true, categories.get(1)),
                    new Product("History of World War II", "Detailed account of World War II events", 24.99, 150, true, categories.get(1)),

                    // Clothing
                    new Product("Men's T-Shirt", "Comfortable cotton t-shirt in various colors", 14.99, 300, true, categories.get(2)),
                    new Product("Women's Jacket", "Stylish jacket with a warm lining", 49.99, 80, true, categories.get(2)),
                    new Product("Children's Jeans", "Durable jeans for children", 19.99, 120, true, categories.get(2)),

                    // Home Appliances
                    new Product("Refrigerator", "Energy-efficient refrigerator with spacious interior", 799.99, 30, true, categories.get(3)),
                    new Product("Washing Machine", "Front-loading washing machine with multiple settings", 499.99, 40, true, categories.get(3)),
                    new Product("Microwave Oven", "Compact microwave oven with quick heating features", 89.99, 70, true, categories.get(3)),

                    // Sports
                    new Product("Running Shoes", "Comfortable running shoes for all terrains", 79.99, 60, true, categories.get(4)),
                    new Product("Tennis Racket", "Professional tennis racket with ergonomic grip", 129.99, 40, true, categories.get(4)),
                    new Product("Yoga Mat", "Non-slip yoga mat for home or studio use", 29.99, 100, true, categories.get(4))
            );

            // Save products to the database
            productRepository.saveAll(products);
            List<Product> products2 = productRepository.findAll();
            List<User> users = userRepository.findAll();
            if (!users.isEmpty() && !products.isEmpty()) {
                List<Review> reviews = List.of(
                        new Review(users.get(0), products.get(0), 5, "Excellent smartphone!", LocalDateTime.now()),
                        new Review(users.get(1), products.get(1), 4, "Great laptop but a bit expensive.", LocalDateTime.now())
                );

                reviews.forEach(review -> {
                    review.getProduct().addReview(review); // Add review to product
                    reviewRepository.save(review);
                });
            } else {
                System.out.println("No users or products found.");
            }

        };
    };

}