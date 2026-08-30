package com.devvault.devvault.exception.dto;

public class SuccessResponse {

    private int status;
    private String message;

    public SuccessResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}