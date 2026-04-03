package dev.krishnaprasad.nexus.exception;

/**
 * Exception thrown when a Nexus resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
