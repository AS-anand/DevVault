package com.devvault.devvault.folder.repository;

import com.devvault.devvault.folder.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByVaultId(Long vaultId);

    Optional<Folder> findByIdAndVaultId(
            Long folderId,
            Long vaultId
    );
}