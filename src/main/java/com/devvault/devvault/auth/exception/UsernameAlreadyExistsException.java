package com.devvault.devvault.auth.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}