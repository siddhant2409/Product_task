package com.productmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "JWT Token Response DTO")
public class JwtTokenResponse {

    @Schema(description = "Access Token")
    private String accessToken;

    @Schema(description = "Refresh Token")
    private String refreshToken;

    @Builder.Default
    @Schema(description = "Token Type", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Expiration Time in milliseconds")
    private Long expiresIn;

    @Schema(description = "Username")
    private String username;
}
