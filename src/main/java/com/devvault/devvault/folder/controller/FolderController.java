package com.devvault.devvault.folder.controller;

import com.devvault.devvault.auth.security.CustomUserPrincipal;
import com.devvault.devvault.folder.dto.CreateFolderRequest;
import com.devvault.devvault.folder.dto.FolderResponse;
import com.devvault.devvault.folder.service.FolderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.devvault.devvault.folder.dto.UpdateFolderRequest;

@RestController
@RequestMapping("/api/v1/vaults/{vaultId}/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FolderResponse createFolder(
            @PathVariable Long vaultId,
            @Valid @RequestBody CreateFolderRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return folderService.createFolder(
                vaultId,
                principal.getId(),
                request
        );
    }

    @GetMapping("/{folderId}")
    public FolderResponse getFolder(
            @PathVariable Long vaultId,
            @PathVariable Long folderId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return folderService.getFolder(
                vaultId,
                folderId,
                principal.getId()
        );
    }

    @GetMapping
    public List<FolderResponse> listFolders(
            @PathVariable Long vaultId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return folderService.listFolders(
                vaultId,
                principal.getId()
        );
    }

    @PatchMapping("/{folderId}")
    public FolderResponse updateFolder(
            @PathVariable Long vaultId,
            @PathVariable Long folderId,
            @Valid @RequestBody UpdateFolderRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return folderService.updateFolder(
                vaultId,
                folderId,
                principal.getId(),
                request
        );
    }

    @DeleteMapping("/{folderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFolder(
            @PathVariable Long vaultId,
            @PathVariable Long folderId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        folderService.deleteFolder(
                vaultId,
                folderId,
                principal.getId()
        );
    }

    @PatchMapping("/{folderId}/secrets/{secretId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moveSecretToFolder(
            @PathVariable Long vaultId,
            @PathVariable Long folderId,
            @PathVariable Long secretId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        folderService.moveSecretToFolder(
                vaultId,
                folderId,
                secretId,
                principal.getId()
        );
    }

    @PatchMapping("/{folderId}/secrets/{secretId}/detach")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void detachSecretFromFolder(
            @PathVariable Long vaultId,
            @PathVariable Long folderId,
            @PathVariable Long secretId,
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        folderService.detachSecretFromFolder(
                vaultId,
                folderId,
                secretId,
                principal.getId()
        );
    }
}