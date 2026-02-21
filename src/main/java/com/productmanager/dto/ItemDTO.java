package com.productmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Item DTO")
public class ItemDTO {

    @Schema(description = "Item ID", example = "1")
    private Long id;

    @NotNull(message = "Product ID cannot be null")
    @Schema(description = "Product ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Item Quantity", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;
}
