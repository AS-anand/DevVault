package com.devvault.devvault.secret.service;

import com.devvault.devvault.secret.dto.CreateSecretRequest;
import com.devvault.devvault.secret.dto.SecretResponse;
import com.devvault.devvault.secret.entity.Secret;
import com.devvault.devvault.secret.repository.SecretRepository;
import com.devvault.devvault.vault.entity.Vault;
import com.devvault.devvault.vault.exception.VaultAccessDeniedException;
import com.devvault.devvault.vault.exception.VaultNotFoundException;
import com.devvault.devvault.vault.repository.VaultRepository;
import org.springframework.stereotype.Service;
import com.devvault.devvault.secret.dto.SecretDetailResponse;
import java.util.List;
import java.util.stream.Collectors;
import com.devvault.devvault.secret.dto.UpdateSecretRequest;
import com.devvault.devvault.secret.exception.SecretNotFoundException;

@Service
public class SecretService {

    private final SecretRepository secretRepository;
    private final VaultRepository vaultRepository;
    private final EncryptionService encryptionService;

    public SecretService(
            SecretRepository secretRepository,
            VaultRepository vaultRepository,
            EncryptionService encryptionService
    ) {
        this.secretRepository = secretRepository;
        this.vaultRepository = vaultRepository;
        this.encryptionService = encryptionService;
    }

    public SecretResponse createSecret(
            Long vaultId,
            Long userId,
            CreateSecretRequest request
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

        String encryptedValue =
                encryptionService.encrypt(request.getValue());

        Secret secret = new Secret();

        secret.setName(request.getName());
        secret.setType(request.getType());
        secret.setUsername(request.getUsername());
        secret.setEncryptedValue(encryptedValue);
        secret.setVault(vault);

        Secret savedSecret = secretRepository.save(secret);

        return new SecretResponse(savedSecret);
    }

    public SecretDetailResponse getSecret(
            Long vaultId,
            Long secretId,
            Long userId
    ) {

        Secret secret = secretRepository
                .findByIdAndVaultId(secretId, vaultId)
                .orElseThrow(() ->
                        new SecretNotFoundException("Secret not found")
                );

        Vault vault = secret.getVault();

        if (!vault.getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        String decryptedValue =
                encryptionService.decrypt(
                        secret.getEncryptedValue()
                );

        return new SecretDetailResponse(
                secret,
                decryptedValue
        );
    }

    public List<SecretResponse> listSecrets(
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

        return secretRepository.findAllByVaultId(vaultId)
                .stream()
                .map(SecretResponse::new)
                .collect(Collectors.toList());
    }

    public SecretResponse updateSecret(
            Long vaultId,
            Long secretId,
            Long userId,
            UpdateSecretRequest request
    ) {

        Secret secret = secretRepository
                .findByIdAndVaultId(secretId, vaultId)
                .orElseThrow(() ->
                        new SecretNotFoundException("Secret not found")
                );

        Vault vault = secret.getVault();

        if (!vault.getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        if (request.getName() != null) {
            secret.setName(request.getName());
        }

        if (request.getType() != null) {
            secret.setType(request.getType());
        }

        if (request.getUsername() != null) {
            secret.setUsername(request.getUsername());
        }

        if (request.getValue() != null) {
            String encryptedValue =
                    encryptionService.encrypt(request.getValue());

            secret.setEncryptedValue(encryptedValue);
        }

        Secret updatedSecret = secretRepository.save(secret);

        return new SecretResponse(updatedSecret);
    }

    public void deleteSecret(
            Long vaultId,
            Long secretId,
            Long userId
    ) {

        Secret secret = secretRepository
                .findByIdAndVaultId(secretId, vaultId)
                .orElseThrow(() ->
                        new SecretNotFoundException("Secret not found")
                );

        Vault vault = secret.getVault();

        if (!vault.getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        secretRepository.delete(secret);
    }
}