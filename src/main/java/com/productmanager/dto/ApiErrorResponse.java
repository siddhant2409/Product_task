package com.productmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "API Error Response DTO")
public class ApiErrorResponse {

    @Schema(description = "HTTP Status Code")
    private int status;

    @Schema(description = "Error Message")
    private String message;

    @Schema(description = "Error Details")
    private String details;

    @Schema(description = "Timestamp of error occurrence")
    private LocalDateTime timestamp;

    @Schema(description = "API Path that caused the error")
    private String path;
}
