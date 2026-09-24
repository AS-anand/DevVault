package com.devvault.devvault.secret.controller;

import com.devvault.devvault.auth.security.CustomUserPrincipal;
import com.devvault.devvault.secret.dto.CreateSecretRequest;
import com.devvault.devvault.secret.dto.SecretResponse;
import com.devvault.devvault.secret.service.SecretService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.devvault.devvault.secret.dto.SecretDetailResponse;
import java.util.List;
import com.devvault.devvault.secret.dto.UpdateSecretRequest;

@RestController
@RequestMapping("/api/v1/vaults/{vaultId}/secrets")
public class SecretController {

    private final SecretService secretService;

    public SecretController(SecretService secretService) {
        this.secretService = secretService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SecretResponse createSecret(
            @PathVariable Long vaultId,
            @Valid @RequestBody CreateSecretRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return secretService.createSecret(
                vaultId,
                principal.getId(),
                request
        );
    }

    @GetMapping("/{secretId}")
    public SecretDetailResponse getSecret(
            @PathVariable Long vaultId,
            @PathVariable Long secretId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return secretService.getSecret(
                vaultId,
                secretId,
                principal.getId()
        );
    }

    @GetMapping
    public List<SecretResponse> listSecrets(
            @PathVariable Long vaultId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return secretService.listSecrets(
                vaultId,
                principal.getId()
        );
    }

    @PatchMapping("/{secretId}")
    public SecretResponse updateSecret(
            @PathVariable Long vaultId,
            @PathVariable Long secretId,
            @Valid @RequestBody UpdateSecretRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return secretService.updateSecret(
                vaultId,
                secretId,
                principal.getId(),
                request
        );
    }

    @DeleteMapping("/{secretId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSecret(
            @PathVariable Long vaultId,
            @PathVariable Long secretId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        secretService.deleteSecret(
                vaultId,
                secretId,
                principal.getId()
        );
    }
}