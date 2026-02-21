package com.productmanager.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException productNotFound(Long id) {
        return new ResourceNotFoundException("Product not found with id: " + id);
    }

    public static ResourceNotFoundException itemNotFound(Long id) {
        return new ResourceNotFoundException("Item not found with id: " + id);
    }

    public static ResourceNotFoundException productNotFoundByName(String name) {
        return new ResourceNotFoundException("Product not found with name: " + name);
    }
}
