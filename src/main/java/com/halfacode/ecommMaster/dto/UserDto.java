package com.halfacode.ecommMaster.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String name;
    private String password;
    private String role;
    private String phoneNumber;
    private List<String> authorities;
    private boolean accountNonLocked;
    private boolean enabled;
}
