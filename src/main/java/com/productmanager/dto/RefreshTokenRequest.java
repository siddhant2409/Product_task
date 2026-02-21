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
@Schema(description = "Refresh Token Request DTO")
public class RefreshTokenRequest {

    @Schema(description = "Refresh Token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refreshToken;
}
