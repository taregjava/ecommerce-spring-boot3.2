package com.halfacode.ecommMaster.authentication;

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

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, CustomUserDetailsService customUserDetailsService, AuthenticationService authenticationService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationService = authenticationService;
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
            user.setAccountNonLocked(true);
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

    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            UserDto userDto = authenticationService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}