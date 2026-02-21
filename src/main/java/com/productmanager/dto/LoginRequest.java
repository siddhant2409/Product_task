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
@Schema(description = "Login Request DTO")
public class LoginRequest {

    @Schema(description = "Username", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "Password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
