package com.devvault.devvault.vault.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

public class UpdateVaultRequest {

    @NotBlank(message = "Vault name cannot be blank")
    @Size(
            max = 100,
            message = "Vault name must not exceed 100 characters"
    )
    private String name;

    @Size(
            max = 500,
            message = "Description must not exceed 500 characters"
    )
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}