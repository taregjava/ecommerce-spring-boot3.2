package com.halfacode.ecommMaster.dto;

import lombok.Getter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    private String jwt;
    private UserDto user;

    public AuthenticationResponse(String jwt, UserDto user) {
        this.jwt = jwt;
        this.user = user;
    }
}
