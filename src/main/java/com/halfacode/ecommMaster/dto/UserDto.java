package com.halfacode.ecommMaster.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private String token;
}
