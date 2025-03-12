package com.halfacode.ecommMaster.util;

import com.halfacode.ecommMaster.models.*;
import com.halfacode.ecommMaster.repositories.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@Transactional
public class DataInitializer {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private  ProductInventoryRepository productInventoryRepository;

    @Autowired
    private CountryRepository countryRepository;
    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            initializeRoles();
            initializeUsersAndLocations();
            initializeCategories();
            initializeProducts();
            initializeReviews();
            initializeDiscounts();
            initializeCountries();
        };
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = List.of(
                    new Role("ROLE_USER"),
                    new Role("ROLE_ADMIN")
            );
            roleRepository.saveAll(roles);
        }
    }

    private void initializeUsersAndLocations() {
        if (userRepository.count() == 0) {
            List<Role> roles = roleRepository.findAll();

            // Create a location
            Location location = Location.builder()
                    .name("Prime Warehouse")
                    .address("123 Main St")
                    .country("USA")
                    .city("New York")
                    .postalCode("10001")
                    .build();
            locationRepository.save(location);

            // Create an admin user and a regular user with the location
            if (!roles.isEmpty()) {
                User adminUser = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .roles(Set.of(roles.get(1)))
                        .tier("Gold")
                        .build();

                User regularUser = User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .roles(Set.of(roles.get(0)))
                        .tier("Basic")
                        .build();

                User userWithLocation = User.builder()
                        .username("john_doe")
                        .password(passwordEncoder.encode("secure_password"))
                        .enabled(true)
                        .accountNonLocked(true)
                        .loyaltyPoints(100)
                        .tier("Silver")
                        .location(location)
                        .build();

                userRepository.saveAll(List.of(adminUser, regularUser, userWithLocation));
            }
        }
    }

    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category("Electronics", "Devices and gadgets"),
                    new Category("Eco-Friendly", "Eco-friendly and sustainable products")
            );
            categoryRepository.saveAll(categories);
        }
    }

    private void initializeProducts() {
        if (productRepository.count() == 0) {
            List<Category> categories = categoryRepository.findAll();
            if (!categories.isEmpty()) {
                List<Product> products = List.of(
                        new Product("Smartphone", "Latest model with high-resolution display", 699.99, 100, true, categories.get(0)),
                        new Product("Yoga Mat", "Non-slip yoga mat for home or studio use", 29.99, 100, true, categories.get(0))
                );
                productRepository.saveAll(products);
            }
        }
    }

    private void initializeReviews() {
        List<User> users = userRepository.findAll();
        List<Product> products = productRepository.findAll();
        if (!users.isEmpty() && !products.isEmpty() && reviewRepository.count() == 0) {
            List<Review> reviews = List.of(
                    new Review(users.get(0), products.get(0), 5, "Excellent smartphone!", LocalDateTime.now()),
                    new Review(users.get(1), products.get(1), 4, "Good yoga mat for the price.", LocalDateTime.now())
            );
            reviewRepository.saveAll(reviews);
        }
    }

    @PostConstruct
    @Transactional
    public void initializeProductInventory() {
        if (productInventoryRepository.count() == 0) {
            List<Product> products = productRepository.findAll();
            List<Location> locations = locationRepository.findAll();

            // If no locations exist, create default ones
            if (locations.isEmpty()) {
                Location defaultLocation1 = new Location(null, "Warehouse A", "123 Street", "USA", "New York", "10001", 40.7128, -74.0060, "192.168.1.1", null);
                Location defaultLocation2 = new Location(null, "Warehouse B", "456 Road", "Canada", "Toronto", "M4B 1B3", 43.6532, -79.3832, "192.168.1.2", null);
                locations = locationRepository.saveAll(List.of(defaultLocation1, defaultLocation2));
            }

            if (!products.isEmpty()) {
                List<ProductInventory> inventories = List.of(
                        new ProductInventory(products.get(0), locations.get(0), 50, true),
                        new ProductInventory(products.get(0), locations.get(1), 30, true),
                        new ProductInventory(products.get(1), locations.get(0), 20, true),
                        new ProductInventory(products.get(1), locations.get(1), 15, false) // Out of stock case
                );

                productInventoryRepository.saveAll(inventories);
            }
        }
    }

    private void initializeDiscounts() {
        if (discountRepository.count() == 0) {
            List<Discount> discounts = List.of(
                    new Discount(null, "WELCOME10", 10.0, LocalDate.now(), LocalDate.now().plusMonths(1)),
                    new Discount(null, "SUMMER15", 15.0, LocalDate.now(), LocalDate.now().plusMonths(2))
            );
            discountRepository.saveAll(discounts);
        }
    }

    private void initializeCountries() {
        if (countryRepository.count() == 0) {
            List<Country> countries = List.of(
                    new Country("United States", "US"),
                    new Country("Canada", "CA"),
                    new Country("Germany", "DE"),
                    new Country("India", "IN"),
                    new Country("United Kingdom", "GB"),
                    new Country("Australia", "AU"),
                    new Country("France", "FR"),
                    new Country("Italy", "IT"),
                    new Country("Spain", "ES"),
                    new Country("Japan", "JP")
            );
            countryRepository.saveAll(countries);
        }
    }

}