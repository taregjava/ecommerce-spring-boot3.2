package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Role;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.RoleRepository;
import com.halfacode.ecommMaster.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
   /* public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }*/

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user, Set<Long> roleIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Fetch roles from the database
        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            roleRepository.findById(roleId).ifPresent(roles::add);
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User getUserFromAuthHeader(String authHeader) {
        // Implementation for extracting user from authorization header if needed
        return new User();
    }
}
