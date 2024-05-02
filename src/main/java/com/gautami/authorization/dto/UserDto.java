package com.gautami.authorization.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto {
    @NotEmpty(message = "username cannot be empty")
    @Size(min = 3)
    private String username;
    @NotEmpty(message = "password cannot be empty")
    @Size(min = 8)
    private String password;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String email;
}
