package com.devvault.devvault.user.dto;

public class UserProfileResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    private String displayName;

    private String bio;

    public UserProfileResponse(
            Long id,
            String username,
            String email,
            String role,
            String displayName,
            String bio
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.displayName = displayName;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }
}