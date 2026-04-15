package com.app.cartapp.dto;

import com.app.cartapp.model.UserType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserType userType;
}
