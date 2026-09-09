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
import com.devvault.devvault.auth.dto.LogoutRequest;
import com.devvault.devvault.auth.dto.ForgotPasswordRequest;
import com.devvault.devvault.auth.dto.ResetPasswordRequest;


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

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse> logout(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody LogoutRequest request
    ) {
        String accessToken = authHeader.substring(7);

        authService.logout(
                accessToken,
                request.getRefreshToken()
        );

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "Logged out successfully"
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<SuccessResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        authService.forgotPassword(request.getEmail());

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "If an account exists with this email, a password reset link has been sent"
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        authService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "Password reset successfully"
                )
        );
    }


}