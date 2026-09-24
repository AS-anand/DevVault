package com.devvault.devvault.vault.exception;

public class VaultAccessDeniedException extends RuntimeException {

    public VaultAccessDeniedException(String message) {
        super(message);
    }
}