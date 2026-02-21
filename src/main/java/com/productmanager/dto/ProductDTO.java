package com.productmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Product DTO")
public class ProductDTO {

    @Schema(description = "Product ID", example = "1")
    private Long id;

    @NotBlank(message = "Product name cannot be blank")
    @Schema(description = "Product Name", example = "Laptop", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productName;

    @Schema(description = "Created By User", example = "admin")
    private String createdBy;

    @Schema(description = "Created Timestamp")
    private LocalDateTime createdOn;

    @Schema(description = "Modified By User", example = "admin")
    private String modifiedBy;

    @Schema(description = "Last Modified Timestamp")
    private LocalDateTime modifiedOn;

    @Min(value = 0, message = "Number of items must be 0 or positive")
    @Schema(description = "Number of Items", example = "0")
    private Integer itemCount;
}
