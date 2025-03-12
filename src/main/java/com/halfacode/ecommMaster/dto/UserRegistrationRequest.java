package com.halfacode.ecommMaster.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserRegistrationRequest {
    private String username;
    private String password;
    private Set<Long> roleIds = new HashSet<>();

    // Getters and Setters
}
