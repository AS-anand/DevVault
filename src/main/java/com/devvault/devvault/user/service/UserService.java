package com.devvault.devvault.user.service;

import com.devvault.devvault.auth.entity.User;
import com.devvault.devvault.auth.exception.EmailAlreadyExistsException;
import com.devvault.devvault.auth.exception.UsernameAlreadyExistsException;
import com.devvault.devvault.auth.repository.UserRepository;
import com.devvault.devvault.auth.service.JwtService;
import com.devvault.devvault.auth.service.RefreshTokenService;
import com.devvault.devvault.auth.service.TokenRevocationService;
import com.devvault.devvault.user.dto.UserProfileResponse;
import com.devvault.devvault.user.exception.InvalidCurrentPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.devvault.devvault.user.exception.UserNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final TokenRevocationService tokenRevocationService;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RefreshTokenService refreshTokenService,
            TokenRevocationService tokenRevocationService,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.tokenRevocationService = tokenRevocationService;
        this.jwtService = jwtService;
    }

    public UserProfileResponse getCurrentUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getDisplayName(),
                user.getBio()
        );
    }

    public void updateUsername(Long userId, String newUsername) {

        if (userRepository.existsByUsername(newUsername)) {
            throw new UsernameAlreadyExistsException(
                    "Username already taken"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        user.setUsername(newUsername);

        userRepository.save(user);
    }

    public void updateEmail(Long userId, String newEmail) {

        if (userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        user.setEmail(newEmail);

        userRepository.save(user);
    }

    public void updateProfile(
            Long userId,
            String displayName,
            String bio
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        if (displayName != null) {
            user.setDisplayName(displayName);
        }

        if (bio != null) {
            user.setBio(bio);
        }

        userRepository.save(user);
    }

    public void changePassword(
            Long userId,
            String currentPassword,
            String newPassword
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        if (!passwordEncoder.matches(
                currentPassword,
                user.getPassword()
        )) {
            throw new InvalidCurrentPasswordException(
                    "Current password is incorrect"
            );
        }

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);
    }

    public void deleteAccount(
            Long userId,
            String accessToken,
            String refreshToken
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        refreshTokenService.deleteRefreshToken(refreshToken);

        String tokenId = jwtService.extractTokenId(accessToken);

        long remainingTime =
                jwtService.extractExpiration(accessToken).getTime()
                        - System.currentTimeMillis();

        if (remainingTime > 0) {
            tokenRevocationService.revokeToken(
                    tokenId,
                    remainingTime
            );
        }

        userRepository.delete(user);
    }
}