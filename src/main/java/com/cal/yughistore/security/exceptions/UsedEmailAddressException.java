package com.cal.yughistore.security.exceptions;

import org.springframework.http.HttpStatus;

public class UsedEmailAddressException extends APIException {
    public UsedEmailAddressException() {
        super(HttpStatus.CONFLICT, "user already used");
    }
}
