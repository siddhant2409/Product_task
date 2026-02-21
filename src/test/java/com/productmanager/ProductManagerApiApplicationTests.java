package com.productmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productmanager.dto.ProductDTO;
import com.productmanager.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@SuppressWarnings("all")
class ProductManagerApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }

    @Test
    @WithMockUser(username = "testuser")
    void testProductCrudOperations() throws Exception {
        // Create Product
        ProductDTO createRequest = ProductDTO.builder()
                .productName("Integration Test Product")
                .build();

        String createResponse = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Integration Test Product"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductDTO createdProduct = objectMapper.readValue(createResponse, ProductDTO.class);

        // Read Product
        mockMvc.perform(get("/api/v1/products/" + createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Integration Test Product"));

        // Get All Products
        mockMvc.perform(get("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(1));

        // Update Product
        ProductDTO updateRequest = ProductDTO.builder()
                .productName("Updated Integration Test Product")
                .build();

        mockMvc.perform(put("/api/v1/products/" + createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Updated Integration Test Product"));

        // Delete Product
        mockMvc.perform(delete("/api/v1/products/" + createdProduct.getId()))
                .andExpect(status().isNoContent());

        // Verify Product is Deleted
        mockMvc.perform(get("/api/v1/products/" + createdProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Product Manager API is running successfully!"));
    }
}
