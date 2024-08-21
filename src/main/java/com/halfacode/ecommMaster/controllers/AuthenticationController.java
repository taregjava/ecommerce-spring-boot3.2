package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.AuthenticationRequest;
import com.halfacode.ecommMaster.dto.AuthenticationResponse;
import com.halfacode.ecommMaster.dto.UserDto;
import com.halfacode.ecommMaster.dto.UserRegistrationRequest;
import com.halfacode.ecommMaster.models.Role;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.security.CustomUserDetails;
import com.halfacode.ecommMaster.security.CustomUserDetailsService;
import com.halfacode.ecommMaster.security.JwtUtil;
import com.halfacode.ecommMaster.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {


    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserService userService;

    private CustomUserDetailsService customUserDetailsService; // Use CustomUserDetailsService

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed: " + result.getAllErrors());
        }  try {
            // Create a new User object
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(registrationRequest.getPassword());
            user.setEnabled(true);

            // Assign roles to the user
            Set<Long> roleIds = registrationRequest.getRoleIds();
            User savedUser = userService.saveUser(user, roleIds);

            // Load the user details
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(savedUser.getUsername());

            // Generate the JWT token
            String jwtToken = jwtUtil.generateToken(userDetails);

            // Create a response object that includes the saved user and the JWT token
            Map<String, Object> response = new HashMap<>();
            response.put("user", savedUser);
            response.put("token", jwtToken);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed");
        }
    }

 /*   @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody AuthenticationRequest authenticationRequest) {
        Logger logger = LoggerFactory.getLogger(AuthenticationController.class);  // Replace 'YourControllerClass' with the actual class name

        try {
            logger.debug("Attempting to authenticate user: {}", authenticationRequest.getUsername());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

            logger.debug("Authentication successful for user: {}", authenticationRequest.getUsername());

            // Load user details
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            logger.debug("Loaded UserDetails for user: {}", userDetails.getUsername());

            // Generate JWT token
            final String jwt = jwtUtil.generateToken(userDetails);
            logger.debug("Generated JWT token for user: {}", userDetails.getUsername());

            // Return the token in the response
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed for user: {}", authenticationRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            logger.error("An unexpected error occurred during authentication for user: {}", authenticationRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody AuthenticationRequest authenticationRequest) {
        Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

        try {
            logger.debug("Attempting to authenticate user: {}", authenticationRequest.getUsername());

            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

            logger.debug("Authentication successful for user: {}", authenticationRequest.getUsername());

            // Load user details using the custom UserDetailsService
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            logger.debug("Loaded UserDetails for user: {}", userDetails.getUsername());

            // Cast to CustomUserDetails to access additional properties
            if (userDetails instanceof CustomUserDetails) {
                CustomUserDetails customUser = (CustomUserDetails) userDetails;
                UserDto userDTO = new UserDto();
                userDTO.setUsername(customUser.getUsername());
                userDTO.setName(customUser.getName());

                // Collect role names as a string (assuming there's only one role per user)
                String roles = customUser.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", "));
                userDTO.setRole(roles);

                List<String> authorities = customUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                userDTO.setAuthorities(authorities);

                // Generate JWT token with extra claims
                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("roles", roles);
                final String jwt = jwtUtil.generateToken(userDetails, extraClaims);
                logger.debug("Generated JWT token for user: {}", userDetails.getUsername());

                // Return the token along with user details in the response
                AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt, userDTO);
                return ResponseEntity.ok(authenticationResponse);
            } else {
                logger.error("Failed to cast UserDetails to CustomUserDetails");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
            }

        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed for user: {}", authenticationRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            logger.error("An unexpected error occurred during authentication for user: {}", authenticationRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }


}