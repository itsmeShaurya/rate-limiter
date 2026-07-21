package com.shaurya.rate_limiter.exception;

/**
 * Thrown when trying to create a user with a username
 * that already exists in the database.
 */
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
