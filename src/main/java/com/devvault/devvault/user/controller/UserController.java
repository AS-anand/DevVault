package com.devvault.devvault.user.controller;

import com.devvault.devvault.auth.security.CustomUserPrincipal;
import com.devvault.devvault.user.dto.*;
import com.devvault.devvault.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.devvault.devvault.exception.dto.SuccessResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        UserProfileResponse response =
                userService.getCurrentUser(principal.getId());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/username")
    public ResponseEntity<SuccessResponse> updateUsername(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody UpdateUsernameRequest request
    ) {
        userService.updateUsername(
                principal.getId(),
                request.getUsername()
        );

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "Username updated successfully"
                )
        );
    }

    @PatchMapping("/me/email")
    public ResponseEntity<SuccessResponse> updateEmail(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody UpdateEmailRequest request
    ) {
        userService.updateEmail(
                principal.getId(),
                request.getEmail()
        );

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "Email updated successfully"
                )
        );
    }

    @PatchMapping("/me/profile")
    public ResponseEntity<SuccessResponse> updateProfile(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        userService.updateProfile(
                principal.getId(),
                request.getDisplayName(),
                request.getBio()
        );

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "Profile updated successfully"
                )
        );
    }

    @PatchMapping("/me/password")
    public ResponseEntity<SuccessResponse> changePassword(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(
                principal.getId(),
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "Password changed successfully"
                )
        );
    }

    @DeleteMapping("/me")
    public ResponseEntity<SuccessResponse> deleteAccount(
            @RequestHeader("Authorization") String authHeader,
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody DeleteAccountRequest request
    ) {
        String accessToken = authHeader.substring(7);

        userService.deleteAccount(
                principal.getId(),
                accessToken,
                request.getRefreshToken()
        );

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "Account deleted successfully"
                )
        );
    }
}