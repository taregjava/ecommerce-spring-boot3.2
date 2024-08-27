package com.halfacode.ecommMaster.authentication;

import com.halfacode.ecommMaster.dto.AuthenticationRequest;
import com.halfacode.ecommMaster.dto.OtpVerificationDto;
import com.halfacode.ecommMaster.dto.UserDto;
import com.halfacode.ecommMaster.dto.UserRegistrationRequest;
import com.halfacode.ecommMaster.mapper.UserMapper;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.security.CustomUserDetailsService;
import com.halfacode.ecommMaster.security.JwtUtil;
import com.halfacode.ecommMaster.services.OtpService;
import com.halfacode.ecommMaster.services.UserService;
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
    private final OtpService otpService;
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, CustomUserDetailsService customUserDetailsService, AuthenticationService authenticationService, OtpService otpService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationService = authenticationService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed: " + result.getAllErrors());
        }
        try {
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setPassword(registrationRequest.getPassword());
            // user.setEnabled(true);
            user.setAccountNonLocked(true);

            Set<Long> roleIds = registrationRequest.getRoleIds();
            User savedUser = userService.saveUser(user, roleIds);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(savedUser.getUsername());

            String jwtToken = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("user", UserMapper.toDTO(savedUser));
            response.put("token", jwtToken);
            response.put("refreshToken", refreshToken);
            String otp = otpService.generateOtp(registrationRequest.getUsername());
            otpService.sendOtp(String.valueOf(registrationRequest.getRoleIds()), otp);
            response.put("otp", otp);
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
}