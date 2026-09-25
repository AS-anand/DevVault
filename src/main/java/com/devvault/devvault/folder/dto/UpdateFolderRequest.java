package com.devvault.devvault.folder.dto;

import jakarta.validation.constraints.Size;

public class UpdateFolderRequest {

    @Size(max = 100)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}