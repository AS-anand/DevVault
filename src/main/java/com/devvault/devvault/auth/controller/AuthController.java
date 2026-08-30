package com.devvault.devvault.auth.controller;

import com.devvault.devvault.auth.dto.RegisterRequest;
import com.devvault.devvault.auth.service.AuthService;
import com.devvault.devvault.exception.dto.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.devvault.devvault.auth.dto.LoginRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(
            @RequestBody RegisterRequest request
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
    public ResponseEntity<SuccessResponse> login(
            @RequestBody LoginRequest request
    ) {
        authService.login(request);

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "Login successful"
                )
        );
    }
}