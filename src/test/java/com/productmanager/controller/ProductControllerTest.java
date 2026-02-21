package com.productmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productmanager.dto.ProductDTO;
import com.productmanager.dto.PagedResponse;
import com.productmanager.service.ProductService;
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

import java.time.LocalDateTime;
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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        productDTO = ProductDTO.builder()
                .id(1L)
                .productName("Test Product")
                .createdBy("admin")
                .createdOn(LocalDateTime.now())
                .itemCount(0)
                .build();
    }

    @Test
    @WithMockUser(username = "admin")
    void createProduct_Success() throws Exception {
        // Arrange
        ProductDTO requestDTO = ProductDTO.builder()
                .productName("Test Product")
                .build();

        when(productService.createProduct(any(ProductDTO.class), eq("admin")))
                .thenReturn(productDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getProductById_Success() throws Exception {
        // Arrange
        when(productService.getProductById(1L)).thenReturn(productDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productName").value("Test Product"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getAllProducts_Success() throws Exception {
        // Arrange
        PagedResponse<ProductDTO> response = PagedResponse.<ProductDTO>builder()
                .content(Arrays.asList(productDTO))
                .pageNumber(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .isFirst(true)
                .isLast(true)
                .build();

        when(productService.getAllProducts(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(username = "admin")
    void updateProduct_Success() throws Exception {
        // Arrange
        ProductDTO updateDTO = ProductDTO.builder()
                .productName("Updated Product")
                .build();

        ProductDTO updatedDTO = ProductDTO.builder()
                .id(1L)
                .productName("Updated Product")
                .createdBy("admin")
                .modifiedBy("admin")
                .modifiedOn(LocalDateTime.now())
                .build();

        when(productService.updateProduct(eq(1L), any(ProductDTO.class), eq("admin")))
                .thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Updated Product"));
    }

    @Test
    @WithMockUser(username = "admin")
    void deleteProduct_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProduct_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isUnauthorized());
    }
}
