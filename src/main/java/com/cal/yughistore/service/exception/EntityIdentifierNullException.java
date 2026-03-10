package com.cal.yughistore.service.exception;

public class EntityIdentifierNullException extends RuntimeException {

    public EntityIdentifierNullException(String message) {
        super(message);
    }

    public EntityIdentifierNullException(Class<?> entityClass, String message) {
        super("Entity "+entityClass.getName()+" ID : "+message);
    }

    public EntityIdentifierNullException(Class<?> entityClass) {
        super("Entity "+entityClass.getName()+" ID : can't be null");
    }
}
