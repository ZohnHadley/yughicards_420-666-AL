package com.cal.yughistore.service.exception.userExceptions;


public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super(message);
    }
}

