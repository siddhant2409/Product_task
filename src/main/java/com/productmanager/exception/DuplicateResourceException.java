package com.productmanager.exception;

public class DuplicateResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DuplicateResourceException productNameExists(String productName) {
        return new DuplicateResourceException("Product with name '" + productName + "' already exists");
    }
}
