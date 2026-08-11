package com.devvault.devvault.auth;

import java.time.LocalDate;

public record RegisterUserCommand(
        String name,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        String password
) {
}