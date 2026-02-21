package com.productmanager.controller;

import com.productmanager.dto.ItemDTO;
import com.productmanager.dto.PagedResponse;
import com.productmanager.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/products/{productId}/items")
@Tag(name = "Items", description = "Item management endpoints")
@SecurityRequirement(name = "bearerAuth")
@SuppressWarnings("null")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    @Operation(summary = "Create a new item", description = "Create a new item for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created successfully",
                    content = @Content(schema = @Schema(implementation = ItemDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ItemDTO> createItem(@PathVariable Long productId,
                                              @Valid @RequestBody ItemDTO itemDTO) {
        log.info("Creating item for product id: {}", productId);
        ItemDTO createdItem = itemService.createItem(productId, itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "Get item by ID", description = "Retrieve an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found",
                    content = @Content(schema = @Schema(implementation = ItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item or product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long productId,
                                               @PathVariable Long itemId) {
        log.info("Fetching item with id: {} for product id: {}", itemId, productId);
        ItemDTO item = itemService.getItemById(productId, itemId);
        return ResponseEntity.ok(item);
    }

    @GetMapping
    @Operation(summary = "Get all items for a product", description = "Retrieve all items for a product with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PagedResponse<ItemDTO>> getItemsByProductId(@PathVariable Long productId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size,
                                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                                       @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        log.info("Fetching items for product id: {} - page: {}, size: {}", productId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PagedResponse<ItemDTO> response = itemService.getItemsByProductId(productId, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "Update item", description = "Update an existing item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully",
                    content = @Content(schema = @Schema(implementation = ItemDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item or product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long productId,
                                              @PathVariable Long itemId,
                                              @Valid @RequestBody ItemDTO itemDTO) {
        log.info("Updating item with id: {} for product id: {}", itemId, productId);
        ItemDTO updatedItem = itemService.updateItem(productId, itemId, itemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete item", description = "Delete an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item or product not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteItem(@PathVariable Long productId,
                                           @PathVariable Long itemId) {
        log.info("Deleting item with id: {} for product id: {}", itemId, productId);
        itemService.deleteItem(productId, itemId);
        return ResponseEntity.noContent().build();
    }
}
