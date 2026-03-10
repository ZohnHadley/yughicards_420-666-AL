package com.cal.yughistore.service.exception;

public class EntityDTONullException extends RuntimeException {

    public EntityDTONullException(String message) {
        super(message);
    }

    public EntityDTONullException(Class<?> entityClass, String message) {
        super("EntityDTO " + entityClass.getName() + " : " + message);
    }

}
