package com.devvault.devvault.secret.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;
    private static final int KEY_LENGTH = 32;

    private final SecretKeySpec secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public EncryptionService(
            @Value("${secret.encryption.key}") String base64Key
    ) {
        byte[] keyBytes;

        try {
            keyBytes = Base64.getDecoder().decode(base64Key);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Invalid Base64 encryption key",
                    e
            );
        }

        if (keyBytes.length != KEY_LENGTH) {
            throw new IllegalStateException(
                    "Encryption key must be exactly 32 bytes"
            );
        }

        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plaintext) {

        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(TAG_LENGTH, iv);

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    parameterSpec
            );

            byte[] ciphertext = cipher.doFinal(
                    plaintext.getBytes(StandardCharsets.UTF_8)
            );

            /*
             * Store:
             *
             * [ IV ][ Ciphertext + Authentication Tag ]
             *
             * Then Base64 encode the complete byte array.
             */
            byte[] combined = ByteBuffer
                    .allocate(iv.length + ciphertext.length)
                    .put(iv)
                    .put(ciphertext)
                    .array();

            return Base64.getEncoder()
                    .encodeToString(combined);

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Failed to encrypt data",
                    e
            );
        }
    }

    public String decrypt(String encryptedData) {

        try {
            byte[] combined = Base64.getDecoder()
                    .decode(encryptedData);

            if (combined.length <= IV_LENGTH) {
                throw new IllegalArgumentException(
                        "Invalid encrypted data"
                );
            }

            byte[] iv = Arrays.copyOfRange(
                    combined,
                    0,
                    IV_LENGTH
            );

            byte[] ciphertext = Arrays.copyOfRange(
                    combined,
                    IV_LENGTH,
                    combined.length
            );

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(TAG_LENGTH, iv);

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    parameterSpec
            );

            byte[] plaintext = cipher.doFinal(ciphertext);

            return new String(
                    plaintext,
                    StandardCharsets.UTF_8
            );

        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Failed to decrypt data",
                    e
            );
        }
    }
}