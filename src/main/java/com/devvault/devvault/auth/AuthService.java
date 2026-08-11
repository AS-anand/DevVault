package com.devvault.devvault.auth;

public class AuthService {

    public User register(RegisterUserCommand command) {
        return new User(Role.USER);
    }
}