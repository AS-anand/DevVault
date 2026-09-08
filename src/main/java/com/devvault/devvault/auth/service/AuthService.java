package com.devvault.devvault.auth.service;

import com.devvault.devvault.auth.dto.RegisterRequest;
import com.devvault.devvault.auth.entity.User;
import com.devvault.devvault.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.devvault.devvault.auth.dto.LoginRequest;
import com.devvault.devvault.auth.exception.EmailAlreadyExistsException;
import com.devvault.devvault.auth.exception.UsernameAlreadyExistsException;
import com.devvault.devvault.auth.exception.InvalidCredentialsException;
import com.devvault.devvault.auth.dto.AuthResponse;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenRevocationService tokenRevocationService;


    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            TokenRevocationService tokenRevocationService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.tokenRevocationService = tokenRevocationService;
    }

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password")
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtService.generateToken(user.getEmail());

        String refreshToken = refreshTokenService.generateRefreshToken();
        refreshTokenService.storeRefreshToken(refreshToken, user.getId());

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {

        Long userId = refreshTokenService.getUserId(refreshToken);

        if (userId == null) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid refresh token")
                );

        refreshTokenService.deleteRefreshToken(refreshToken);

        String newAccessToken = jwtService.generateToken(user.getEmail());

        String newRefreshToken = refreshTokenService.generateRefreshToken();
        refreshTokenService.storeRefreshToken(newRefreshToken, user.getId());

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String accessToken, String refreshToken) {

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
    }
}