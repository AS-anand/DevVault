package com.devvault.devvault.vault.service;

import com.devvault.devvault.auth.entity.User;
import com.devvault.devvault.auth.repository.UserRepository;
import com.devvault.devvault.vault.dto.CreateVaultRequest;
import com.devvault.devvault.vault.dto.VaultResponse;
import com.devvault.devvault.vault.entity.Vault;
import com.devvault.devvault.vault.repository.VaultRepository;
import org.springframework.stereotype.Service;
import com.devvault.devvault.vault.exception.VaultAccessDeniedException;
import com.devvault.devvault.vault.exception.VaultNotFoundException;
import java.util.List;
import com.devvault.devvault.vault.dto.UpdateVaultRequest;

@Service
public class VaultService {

    private final VaultRepository vaultRepository;
    private final UserRepository userRepository;

    public VaultService(
            VaultRepository vaultRepository,
            UserRepository userRepository
    ) {
        this.vaultRepository = vaultRepository;
        this.userRepository = userRepository;
    }

    public VaultResponse createVault(
            Long userId,
            CreateVaultRequest request
    ) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Vault vault = new Vault();

        vault.setName(request.getName());
        vault.setDescription(request.getDescription());
        vault.setOwner(owner);

        Vault savedVault = vaultRepository.save(vault);

        return new VaultResponse(
                savedVault.getId(),
                savedVault.getName(),
                savedVault.getDescription(),
                savedVault.getOwner().getId(),
                savedVault.getCreatedAt(),
                savedVault.getUpdatedAt()
        );
    }

    public VaultResponse getVault(
            Long vaultId,
            Long userId
    ) {
        Vault vault = vaultRepository.findById(vaultId)
                .orElseThrow(() ->
                        new VaultNotFoundException("Vault not found")
                );

        if (!vault.getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        return new VaultResponse(
                vault.getId(),
                vault.getName(),
                vault.getDescription(),
                vault.getOwner().getId(),
                vault.getCreatedAt(),
                vault.getUpdatedAt()
        );
    }

    public List<VaultResponse> getUserVaults(Long userId) {

        return vaultRepository.findAllByOwnerId(userId)
                .stream()
                .map(vault -> new VaultResponse(
                        vault.getId(),
                        vault.getName(),
                        vault.getDescription(),
                        vault.getOwner().getId(),
                        vault.getCreatedAt(),
                        vault.getUpdatedAt()
                ))
                .toList();
    }

    public VaultResponse updateVault(
            Long vaultId,
            Long userId,
            UpdateVaultRequest request
    ) {
        Vault vault = vaultRepository.findById(vaultId)
                .orElseThrow(() ->
                        new VaultNotFoundException("Vault not found")
                );

        if (!vault.getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        if (request.getName() != null) {
            vault.setName(request.getName());
        }

        if (request.getDescription() != null) {
            vault.setDescription(request.getDescription());
        }

        Vault updatedVault = vaultRepository.save(vault);

        return new VaultResponse(
                updatedVault.getId(),
                updatedVault.getName(),
                updatedVault.getDescription(),
                updatedVault.getOwner().getId(),
                updatedVault.getCreatedAt(),
                updatedVault.getUpdatedAt()
        );
    }

    public void deleteVault(
            Long vaultId,
            Long userId
    ) {
        Vault vault = vaultRepository.findById(vaultId)
                .orElseThrow(() ->
                        new VaultNotFoundException("Vault not found")
                );

        if (!vault.getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        vaultRepository.delete(vault);
    }
}