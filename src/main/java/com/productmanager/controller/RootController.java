package com.productmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
@Tag(name = "Health", description = "Health check endpoints")
public class RootController {

    @GetMapping
    @Operation(summary = "Health check", description = "Check if the API is running")
    public ResponseEntity<String> health() {
        log.info("Health check endpoint called");
        return ResponseEntity.ok("Product Manager API is running successfully!");
    }

    @GetMapping("/health")
    @Operation(summary = "Detailed health check", description = "Get detailed health status of the API")
    public ResponseEntity<HealthStatus> getHealthStatus() {
        log.info("Detailed health check endpoint called");
        return ResponseEntity.ok(HealthStatus.builder()
                .status("UP")
                .message("Product Manager API is healthy")
                .timestamp(System.currentTimeMillis())
                .build());
    }

    @lombok.Data
    @lombok.Builder
    public static class HealthStatus {
        private String status;
        private String message;
        private long timestamp;
    }
}
