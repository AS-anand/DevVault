package com.devvault.devvault.secret.dto;

import jakarta.validation.constraints.Size;

public class UpdateSecretRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String type;

    @Size(max = 255)
    private String username;

    private String value;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setValue(String value) {
        this.value = value;
    }
}