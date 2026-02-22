package com.cal.yughistore.security.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends APIException{
        public UserNotFoundException(String s) {
            super(HttpStatus.NOT_FOUND,"userNotFound");
        }
}
