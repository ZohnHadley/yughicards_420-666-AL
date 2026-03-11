package com.cal.yughistore.service.exception.util;

import java.io.IOException;

public class FileProcessingException extends RuntimeException {
    public FileProcessingException(String message, IOException e) {
        super(message);
    }
    public FileProcessingException(String message) {
        super(message);
    }
}
