package com.productmanager.service;

import com.productmanager.dto.ProductDTO;
import com.productmanager.dto.PagedResponse;
import com.productmanager.entity.Product;
import com.productmanager.exception.DuplicateResourceException;
import com.productmanager.exception.ResourceNotFoundException;
import com.productmanager.repository.ProductRepository;
import com.productmanager.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        productDTO = ProductDTO.builder()
                .productName("Test Product")
                .build();

        product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .createdBy("admin")
                .createdOn(LocalDateTime.now())
                .build();
    }

    @Test
    void createProduct_Success() {
        // Arrange
        when(productRepository.countByProductNameIgnoreCase(anyString())).thenReturn(0L);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductDTO result = productService.createProduct(productDTO, "admin");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProductName()).isEqualTo("Test Product");
        assertThat(result.getCreatedBy()).isEqualTo("admin");
        verify(productRepository, times(1)).countByProductNameIgnoreCase(anyString());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_DuplicateProductName() {
        // Arrange
        when(productRepository.countByProductNameIgnoreCase(anyString())).thenReturn(1L);

        // Act & Assert
        assertThatThrownBy(() -> productService.createProduct(productDTO, "admin"))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void getProductById_Success() {
        // Arrange
        when(productRepository.findByIdWithItems(1L)).thenReturn(Optional.of(product));

        // Act
        ProductDTO result = productService.getProductById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProductName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).findByIdWithItems(1L);
    }

    @Test
    void getProductById_NotFound() {
        // Arrange
        when(productRepository.findByIdWithItems(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.getProductById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void getAllProducts_Success() {
        // Arrange
        List<Product> products = Arrays.asList(product);
        Page<Product> page = new PageImpl<>(products, PageRequest.of(0, 10), 1);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        PagedResponse<ProductDTO> result = productService.getAllProducts(PageRequest.of(0, 10));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getPageNumber()).isZero();
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateProduct_Success() {
        // Arrange
        ProductDTO updateDTO = ProductDTO.builder()
                .productName("Updated Product")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.countByProductNameIgnoreCase(anyString())).thenReturn(0L);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductDTO result = productService.updateProduct(1L, updateDTO, "admin");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getModifiedBy()).isEqualTo("admin");
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void deleteProduct_NotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
