package com.devvault.devvault.secret.dto;

import com.devvault.devvault.secret.entity.Secret;

import java.time.LocalDateTime;

public class SecretResponse {

    private Long id;
    private String name;
    private String type;
    private String username;
    private Long vaultId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SecretResponse(Secret secret) {
        this.id = secret.getId();
        this.name = secret.getName();
        this.type = secret.getType();
        this.username = secret.getUsername();
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