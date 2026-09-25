package com.devvault.devvault.secret.repository;

import com.devvault.devvault.secret.entity.Secret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SecretRepository extends JpaRepository<Secret, Long> {

    Optional<Secret> findByIdAndVaultId(Long secretId, Long vaultId);

    List<Secret> findAllByVaultId(Long vaultId);

    List<Secret> findAllByFolderId(Long folderId);
}