package com.gautami.authorization.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDto {

    private String username;
    private String password;
    private String email;
}
