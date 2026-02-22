package com.cal.yughistore.security.exceptions;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends APIException{
    public AuthenticationException(HttpStatus status, String message) {
        super(status, message);
    }
}
