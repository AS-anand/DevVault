package com.devvault.devvault.auth.controller;

import com.devvault.devvault.auth.dto.RegisterRequest;
import com.devvault.devvault.auth.service.AuthService;
import com.devvault.devvault.exception.dto.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.devvault.devvault.auth.dto.LoginRequest;
import com.devvault.devvault.auth.dto.AuthResponse;
import jakarta.validation.Valid;
import com.devvault.devvault.auth.dto.RefreshTokenRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SuccessResponse(
                        HttpStatus.CREATED.value(),
                        "User registered successfully"
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        AuthResponse response =
                authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(response);
    }


}