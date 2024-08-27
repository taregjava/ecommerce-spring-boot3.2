package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.UserDto;
import com.halfacode.ecommMaster.models.Role;
import com.halfacode.ecommMaster.models.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static UserDto toDTO(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getUsername())
                .password(user.getPassword())
                .role(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.joining(", ")))
                //.phoneNumber(user.getPhoneNumber())
                //.authorities(user.getAuthorities())
                .accountNonLocked(user.isAccountNonLocked())
                .enabled(user.isEnabled())
                // .token(user.getToken()) // Assuming you store JWT tokens in the User entity
                .build();
    }
    public UserDto toDTOCart(User user) {
        return UserDto.builder()
              //  .id(user.getId())
                .username(user.getUsername())
                .build();
    }
    public User toEntity(UserDto userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setUsername(userDTO.getName());
        user.setPassword(userDTO.getPassword());
       // user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAccountNonLocked(userDTO.isAccountNonLocked());
        user.setEnabled(userDTO.isEnabled());

        // Convert roles from String back to Role entities
        user.setRoles((Set<Role>) userDTO.getAuthorities().stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return role;
                })
                .collect(Collectors.toList()));

        return user;
    }
}
