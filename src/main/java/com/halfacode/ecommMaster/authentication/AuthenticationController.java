package com.halfacode.ecommMaster.authentication;

import com.halfacode.ecommMaster.dto.*;
import com.halfacode.ecommMaster.mapper.UserMapper;
import com.halfacode.ecommMaster.models.Location;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.LocationRepository;
import com.halfacode.ecommMaster.repositories.UserRepository;
import com.halfacode.ecommMaster.security.CustomUserDetailsService;
import com.halfacode.ecommMaster.security.JwtUtil;
import com.halfacode.ecommMaster.services.GeoLocationService;
import com.halfacode.ecommMaster.services.LocationService;
import com.halfacode.ecommMaster.services.OtpService;
import com.halfacode.ecommMaster.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationService authenticationService;
    private final GeoLocationService geoLocationService;
    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final UserRepository userRepository;
    private final OtpService otpService;
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, CustomUserDetailsService customUserDetailsService, AuthenticationService authenticationService, GeoLocationService geoLocationService, LocationRepository locationRepository, LocationService locationService, UserRepository userRepository, OtpService otpService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationService = authenticationService;
        this.geoLocationService = geoLocationService;
        this.locationRepository = locationRepository;
        this.locationService = locationService;
        this.userRepository = userRepository;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest,
                                          BindingResult result,
                                          HttpServletRequest request) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed: " + result.getAllErrors());
        }

        try {
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(registrationRequest.getPassword());
            user.setAccountNonLocked(true);
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = request.getRemoteAddr(); // Get IP if not behind a proxy
            }
            // Get the location based on the request IP
            GeoLocationResponse geoLocation = geoLocationService.getGeoLocationForRegistration(request);

            // Save the location in the database
            Location location = new Location();
            location.setCountry(geoLocation.getCountry());
            location.setCity(geoLocation.getCity());
            location.setLatitude(geoLocation.getLat());
            location.setLongitude(geoLocation.getLon());
            location.setIpAddress(ipAddress); // Save the IP address
            location = locationRepository.save(location);

            // Assign the location to the user
            user.setLocation(location);

            // Save user with role
            Set<Long> roleIds = registrationRequest.getRoleIds();
            User savedUser = userService.saveUser(user, roleIds);

            // Generate JWT token
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(savedUser.getUsername());
            String jwtToken = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("user", UserMapper.toDTO(savedUser));
            response.put("token", jwtToken);
            response.put("refreshToken", refreshToken);
            response.put("location", geoLocation); // Return location info

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed: " + e.getMessage());
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

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            String newJwtToken = jwtUtil.generateToken(userDetails);

            Map<String, String> response = new HashMap<>();
            response.put("token", newJwtToken);
            response.put("refreshToken", refreshToken);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationDto otpVerificationDto) {
        boolean isValid = otpService.verifyOtp(otpVerificationDto.getUsername(), otpVerificationDto.getOtp());

        if (isValid) {
            boolean isActivated = authenticationService.activateUser(otpVerificationDto.getUsername());
            if (isActivated) {
                return ResponseEntity.ok("Account activated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User account could not be activated.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
        }
    }

    @PostMapping("/update-location")
    public ResponseEntity<?> updateLocation(@RequestBody LocationUpdateRequest locationUpdateRequest) {
        if (locationUpdateRequest.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is required");
        }

        // Find user by ID
        User user = userRepository.findById(locationUpdateRequest.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Update or create a location for the user
        Location location = user.getLocation() != null ? user.getLocation() : new Location();
        location.setCountry(locationUpdateRequest.getCountry());
        location.setCity(locationUpdateRequest.getCity());
        location.setLatitude(locationUpdateRequest.getLatitude());
        location.setLongitude(locationUpdateRequest.getLongitude());

        // Save the location
        location = locationRepository.save(location);
        user.setLocation(location);
        userRepository.save(user);

        return ResponseEntity.ok("User location updated successfully");
    }


}