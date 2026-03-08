package com.cal.yughistore.service.exception;

public class UserSettingsNotFoundException extends RuntimeException {
    public UserSettingsNotFoundException(String message) {
        super(message);
    }
}