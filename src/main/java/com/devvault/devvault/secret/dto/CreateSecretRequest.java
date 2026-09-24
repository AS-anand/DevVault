package com.devvault.devvault.secret.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSecretRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String type;

    @Size(max = 255)
    private String username;

    @NotBlank
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