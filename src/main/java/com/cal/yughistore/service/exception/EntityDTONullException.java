package com.cal.yughistore.service.exception;

public class EntityDTONullException extends RuntimeException {

    public EntityDTONullException(String message) {
        super(message);
    }

    public EntityDTONullException(Class<?> entityClass, String message) {
        super("EntityDTO " + entityClass.getName() + " : " + message);
    }

    public EntityDTONullException(Class<?> entityClass) {
        super("EntityDTO " + entityClass.getName() + " : can't be null");
    }

}
