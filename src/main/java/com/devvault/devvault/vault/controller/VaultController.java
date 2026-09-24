package com.devvault.devvault.vault.controller;

import com.devvault.devvault.auth.security.CustomUserPrincipal;
import com.devvault.devvault.vault.dto.CreateVaultRequest;
import com.devvault.devvault.vault.dto.VaultResponse;
import com.devvault.devvault.vault.service.VaultService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.devvault.devvault.vault.dto.UpdateVaultRequest;
import com.devvault.devvault.exception.dto.SuccessResponse;

@RestController
@RequestMapping("/api/v1/vaults")
public class VaultController {

    private final VaultService vaultService;

    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @PostMapping
    public ResponseEntity<VaultResponse> createVault(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody CreateVaultRequest request
    ) {
        VaultResponse response =
                vaultService.createVault(
                        principal.getId(),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{vaultId}")
    public ResponseEntity<VaultResponse> getVault(
            @PathVariable Long vaultId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        VaultResponse response =
                vaultService.getVault(
                        vaultId,
                        principal.getId()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VaultResponse>> getUserVaults(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        List<VaultResponse> response =
                vaultService.getUserVaults(principal.getId());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{vaultId}")
    public ResponseEntity<VaultResponse> updateVault(
            @PathVariable Long vaultId,
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody UpdateVaultRequest request
    ) {
        VaultResponse response =
                vaultService.updateVault(
                        vaultId,
                        principal.getId(),
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{vaultId}")
    public ResponseEntity<SuccessResponse> deleteVault(
            @PathVariable Long vaultId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        vaultService.deleteVault(
                vaultId,
                principal.getId()
        );

        return ResponseEntity.ok(
                new SuccessResponse(
                        HttpStatus.OK.value(),
                        "Vault deleted successfully"
                )
        );
    }
}