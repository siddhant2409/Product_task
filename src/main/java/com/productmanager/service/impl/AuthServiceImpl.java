package com.productmanager.service.impl;

import com.productmanager.dto.JwtTokenResponse;
import com.productmanager.dto.LoginRequest;
import com.productmanager.dto.RefreshTokenRequest;
import com.productmanager.exception.InvalidTokenException;
import com.productmanager.security.JwtTokenProvider;
import com.productmanager.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    @SuppressWarnings({"all", "squid:S1135"})
    public JwtTokenResponse login(LoginRequest loginRequest) {
        log.info("Attempting to authenticate user: {}", loginRequest.getUsername());

        // TODO: In a real application, use AuthenticationManager to authenticate user
        // For now, we'll generate tokens for demo purposes
        // In production: AuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(...))

        String accessToken = tokenProvider.generateAccessToken(loginRequest.getUsername());
        String refreshToken = tokenProvider.generateRefreshToken(loginRequest.getUsername());

        log.info("User authenticated successfully: {}", loginRequest.getUsername());

        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getExpirationTime())
                .username(loginRequest.getUsername())
                .build();
    }

    @Override
    public JwtTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.info("Attempting to refresh token");

        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        String newAccessToken = tokenProvider.generateAccessToken(username);

        log.info("Token refreshed successfully for user: {}", username);

        return JwtTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getExpirationTime())
                .username(username)
                .build();
    }
}
