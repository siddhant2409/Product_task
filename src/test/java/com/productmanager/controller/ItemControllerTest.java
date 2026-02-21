package com.productmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productmanager.dto.ItemDTO;
import com.productmanager.dto.PagedResponse;
import com.productmanager.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SuppressWarnings("all")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDTO itemDTO;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        itemDTO = ItemDTO.builder()
                .id(1L)
                .productId(1L)
                .quantity(100)
                .build();
    }

    @Test
    @WithMockUser(username = "admin")
    void createItem_Success() throws Exception {
        // Arrange
        ItemDTO requestDTO = ItemDTO.builder()
                .productId(1L)
                .quantity(100)
                .build();

        when(itemService.createItem(eq(1L), any(ItemDTO.class)))
                .thenReturn(itemDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/products/1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    @WithMockUser(username = "admin")
    void getItemById_Success() throws Exception {
        // Arrange
        when(itemService.getItemById(1L, 1L)).thenReturn(itemDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/1/items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value(1));
    }

    @Test
    @WithMockUser(username = "admin")
    void getItemsByProductId_Success() throws Exception {
        // Arrange
        PagedResponse<ItemDTO> response = PagedResponse.<ItemDTO>builder()
                .content(Arrays.asList(itemDTO))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .isFirst(true)
                .isLast(true)
                .build();

        when(itemService.getItemsByProductId(eq(1L), any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/1/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateItem_Success() throws Exception {
        // Arrange
        ItemDTO updateDTO = ItemDTO.builder()
                .productId(1L)
                .quantity(200)
                .build();

        ItemDTO updatedDTO = ItemDTO.builder()
                .id(1L)
                .productId(1L)
                .quantity(200)
                .build();

        when(itemService.updateItem(eq(1L), eq(1L), any(ItemDTO.class)))
                .thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/api/v1/products/1/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(200));
    }

    @Test
    @WithMockUser(username = "admin")
    void deleteItem_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/products/1/items/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createItem_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/products/1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isUnauthorized());
    }
}
