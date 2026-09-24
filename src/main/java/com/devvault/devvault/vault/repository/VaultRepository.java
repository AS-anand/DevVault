package com.devvault.devvault.vault.repository;

import com.devvault.devvault.vault.entity.Vault;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaultRepository extends JpaRepository<Vault, Long> {

    List<Vault> findAllByOwnerId(Long ownerId);
}