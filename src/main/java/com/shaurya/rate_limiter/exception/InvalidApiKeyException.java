package com.shaurya.rate_limiter.exception;

/**
 * Thrown when a client sends an invalid API Key.
 */
public class InvalidApiKeyException extends RuntimeException{
    public InvalidApiKeyException(String message) {
        super(message);
    }
}
