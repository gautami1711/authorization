package com.gautami.authorization.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class Forbidden extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public Forbidden(String message) {
        super(message);
    }


}
