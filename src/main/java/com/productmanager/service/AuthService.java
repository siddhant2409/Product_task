package com.productmanager.service;

import com.productmanager.dto.JwtTokenResponse;
import com.productmanager.dto.LoginRequest;
import com.productmanager.dto.RefreshTokenRequest;

public interface AuthService {

    JwtTokenResponse login(LoginRequest loginRequest);

    JwtTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
