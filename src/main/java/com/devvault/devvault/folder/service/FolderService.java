package com.devvault.devvault.folder.service;

import com.devvault.devvault.folder.dto.CreateFolderRequest;
import com.devvault.devvault.folder.dto.FolderResponse;
import com.devvault.devvault.folder.entity.Folder;
import com.devvault.devvault.folder.repository.FolderRepository;
import com.devvault.devvault.vault.entity.Vault;
import com.devvault.devvault.vault.exception.VaultAccessDeniedException;
import com.devvault.devvault.vault.exception.VaultNotFoundException;
import com.devvault.devvault.vault.repository.VaultRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.devvault.devvault.folder.dto.UpdateFolderRequest;
import com.devvault.devvault.secret.entity.Secret;
import com.devvault.devvault.secret.repository.SecretRepository;
import com.devvault.devvault.folder.exception.FolderNotFoundException;
import com.devvault.devvault.folder.exception.SecretFolderMismatchException;
import com.devvault.devvault.secret.exception.SecretNotFoundException;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final VaultRepository vaultRepository;
    private final SecretRepository secretRepository;

    public FolderService(
            FolderRepository folderRepository,
            VaultRepository vaultRepository,
            SecretRepository secretRepository
    ) {
        this.folderRepository = folderRepository;
        this.vaultRepository = vaultRepository;
        this.secretRepository = secretRepository;
    }

    public FolderResponse createFolder(
            Long vaultId,
            Long userId,
            CreateFolderRequest request
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

        Folder folder = new Folder();

        folder.setName(request.getName());
        folder.setVault(vault);

        Folder savedFolder = folderRepository.save(folder);

        return new FolderResponse(savedFolder);
    }

    public FolderResponse getFolder(
            Long vaultId,
            Long folderId,
            Long userId
    ) {

        Folder folder = folderRepository
                .findByIdAndVaultId(folderId, vaultId)
                .orElseThrow(() ->
                        new FolderNotFoundException("Folder not found")
                );

        if (!folder.getVault().getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        return new FolderResponse(folder);
    }

    public List<FolderResponse> listFolders(
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

        return folderRepository.findAllByVaultId(vaultId)
                .stream()
                .map(FolderResponse::new)
                .toList();
    }

    public FolderResponse updateFolder(
            Long vaultId,
            Long folderId,
            Long userId,
            UpdateFolderRequest request
    ) {

        Folder folder = folderRepository
                .findByIdAndVaultId(folderId, vaultId)
                .orElseThrow(() ->
                        new FolderNotFoundException("Folder not found")
                );

        if (!folder.getVault().getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        if (request.getName() != null) {
            folder.setName(request.getName());
        }

        Folder updatedFolder = folderRepository.save(folder);

        return new FolderResponse(updatedFolder);
    }

    public void deleteFolder(
            Long vaultId,
            Long folderId,
            Long userId
    ) {

        Folder folder = folderRepository
                .findByIdAndVaultId(folderId, vaultId)
                .orElseThrow(() ->
                        new FolderNotFoundException("Folder not found")
                );

        if (!folder.getVault().getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        List<Secret> secrets =
                secretRepository.findAllByFolderId(folderId);

        for (Secret secret : secrets) {
            secret.setFolder(null);
        }

        secretRepository.saveAll(secrets);

        folderRepository.delete(folder);
    }

    public void moveSecretToFolder(
            Long vaultId,
            Long folderId,
            Long secretId,
            Long userId
    ) {

        Folder folder = folderRepository
                .findByIdAndVaultId(folderId, vaultId)
                .orElseThrow(() ->
                        new FolderNotFoundException("Folder not found")
                );

        if (!folder.getVault().getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        Secret secret = secretRepository
                .findByIdAndVaultId(secretId, vaultId)
                .orElseThrow(() ->
                        new SecretNotFoundException("Secret not found")
                );

        secret.setFolder(folder);

        secretRepository.save(secret);
    }

    public void detachSecretFromFolder(
            Long vaultId,
            Long folderId,
            Long secretId,
            Long userId
    ) {

        Folder folder = folderRepository
                .findByIdAndVaultId(folderId, vaultId)
                .orElseThrow(() ->
                        new FolderNotFoundException("Folder not found")
                );

        if (!folder.getVault().getOwner().getId().equals(userId)) {
            throw new VaultAccessDeniedException(
                    "You do not have access to this vault"
            );
        }

        Secret secret = secretRepository
                .findByIdAndVaultId(secretId, vaultId)
                .orElseThrow(() ->
                        new SecretNotFoundException("Secret not found")
                );

        // Make sure this secret is actually inside this folder.
        if (secret.getFolder() == null ||
                !secret.getFolder().getId().equals(folderId)) {
            throw new SecretFolderMismatchException(
                    "Secret does not belong to this folder"
            );
        }

        secret.setFolder(null);

        secretRepository.save(secret);
    }
}