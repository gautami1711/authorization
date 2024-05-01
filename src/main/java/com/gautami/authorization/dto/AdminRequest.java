package com.gautami.authorization.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRequest {
    private String username;
    private String password;
    private String email;
    private String key;
}
