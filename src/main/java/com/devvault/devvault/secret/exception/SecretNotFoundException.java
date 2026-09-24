package com.devvault.devvault.secret.exception;

public class SecretNotFoundException extends RuntimeException {

    public SecretNotFoundException(String message) {
        super(message);
    }
}