package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.services.LoyaltyProgramService;
import com.halfacode.ecommMaster.services.UserService; // Assuming you have a UserService to fetch User
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyController {


    /*
     User Actions: This method can be called whenever a user makes a purchase or
     performs an action that should earn loyalty points. This could be triggered from
     a checkout process in an e-commerce site.
     Backend Services: Other backend services or scheduled tasks might
     also invoke this method based on specific business logic or promotions.


     Integration Points
     Frontend: Your frontend application (web or mobile)
     can call this endpoint when a user completes a purchase.
     Backend: Internal services can call this method
     when certain conditions are met, like completing an order.

     */
    @Autowired
    private LoyaltyProgramService loyaltyProgramService;

    @Autowired
    private UserService userService; // Service to fetch user information

    @PostMapping("/addPoints")
    public ResponseEntity<String> addLoyaltyPoints(
            @RequestParam Long userId,
            @RequestParam double amountSpent,
            @RequestParam String productCategory,
            @RequestParam(required = false) String promoCode) {

        try {
            // Retrieve user by ID
            User user = userService.getUserById(userId);
                   // .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            // Validate amount spent
            if (amountSpent <= 0) {
                return ResponseEntity.badRequest().body("Amount spent must be greater than zero.");
            }

            // Add loyalty points with promoCode
            loyaltyProgramService.addLoyaltyPoints(user, amountSpent, productCategory, promoCode);

            // Return success response
            return ResponseEntity.ok("Loyalty points added successfully.");
        } catch (IllegalArgumentException e) {
            // Handle not found or validation errors
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


}
