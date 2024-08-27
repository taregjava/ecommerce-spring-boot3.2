package com.halfacode.ecommMaster.authentication;

import com.halfacode.ecommMaster.dto.UserDto;
import com.halfacode.ecommMaster.mapper.UserMapper;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.UserRepository;
import com.halfacode.ecommMaster.security.JwtUtil;
import com.halfacode.ecommMaster.security.CustomUserDetails;
import com.halfacode.ecommMaster.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserDto authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (userDetails instanceof CustomUserDetails) {
                CustomUserDetails customUser = (CustomUserDetails) userDetails;

                UserDto userDto = userMapper.toDTO(customUser.getUser());

                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("roles", userDto.getRole());

                String jwt = jwtUtil.generateToken(userDetails, extraClaims);
                userDto.setToken(jwt);

                return userDto;
            } else {
                throw new Exception("Failed to authenticate user");
            }
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }
    }

    public boolean activateUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.isEnabled()) {
                user.setEnabled(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}