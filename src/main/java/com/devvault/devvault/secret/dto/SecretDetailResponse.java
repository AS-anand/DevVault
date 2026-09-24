package com.devvault.devvault.secret.dto;

import com.devvault.devvault.secret.entity.Secret;

import java.time.LocalDateTime;

public class SecretDetailResponse {

    private Long id;
    private String name;
    private String type;
    private String username;
    private String value;
    private Long vaultId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SecretDetailResponse(
            Secret secret,
            String decryptedValue
    ) {
        this.id = secret.getId();
        this.name = secret.getName();
        this.type = secret.getType();
        this.username = secret.getUsername();
        this.value = decryptedValue;
        this.vaultId = secret.getVault().getId();
        this.createdAt = secret.getCreatedAt();
        this.updatedAt = secret.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

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

    public Long getVaultId() {
        return vaultId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}