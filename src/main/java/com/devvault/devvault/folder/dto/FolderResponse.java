package com.devvault.devvault.folder.dto;

import com.devvault.devvault.folder.entity.Folder;

import java.time.LocalDateTime;

public class FolderResponse {

    private Long id;
    private String name;
    private Long vaultId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FolderResponse(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.vaultId = folder.getVault().getId();
        this.createdAt = folder.getCreatedAt();
        this.updatedAt = folder.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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