package com.gautami.authorization.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AlreadyExists(String message) {
        super(message);
    }


}
