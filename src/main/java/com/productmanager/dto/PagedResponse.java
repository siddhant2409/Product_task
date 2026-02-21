package com.productmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Paginated Response DTO")
public class PagedResponse<T> {

    @Schema(description = "List of items")
    private List<T> content;

    @Schema(description = "Current page number (0-indexed)")
    private int pageNumber;

    @Schema(description = "Page size")
    private int pageSize;

    @Schema(description = "Total number of elements")
    private long totalElements;

    @Schema(description = "Total number of pages")
    private int totalPages;

    @Schema(description = "Is last page")
    private boolean isLast;

    @Schema(description = "Is first page")
    private boolean isFirst;
}
