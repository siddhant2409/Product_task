package com.productmanager.service;

import com.productmanager.dto.ItemDTO;
import com.productmanager.dto.PagedResponse;
import com.productmanager.entity.Item;
import com.productmanager.entity.Product;
import com.productmanager.exception.ResourceNotFoundException;
import com.productmanager.repository.ItemRepository;
import com.productmanager.repository.ProductRepository;
import com.productmanager.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private ItemDTO itemDTO;
    private Item item;
    private Product product;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        itemDTO = ItemDTO.builder()
                .productId(1L)
                .quantity(100)
                .build();

        product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .createdBy("admin")
                .build();

        item = Item.builder()
                .id(1L)
                .product(product)
                .quantity(100)
                .build();
    }

    @Test
    void createItem_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // Act
        ItemDTO result = itemService.createItem(1L, itemDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getQuantity()).isEqualTo(100);
        verify(productRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void createItem_ProductNotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> itemService.createItem(1L, itemDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getItemById_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(itemRepository.findByIdAndProductId(1L, 1L)).thenReturn(Optional.of(item));

        // Act
        ItemDTO result = itemService.getItemById(1L, 1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getQuantity()).isEqualTo(100);
        verify(productRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findByIdAndProductId(1L, 1L);
    }

    @Test
    void getItemsByProductId_Success() {
        // Arrange
        List<Item> items = Arrays.asList(item);
        Page<Item> page = new PageImpl<>(items, PageRequest.of(0, 10), 1);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(itemRepository.findByProductId(1L, PageRequest.of(0, 10))).thenReturn(page);

        // Act
        PagedResponse<ItemDTO> result = itemService.getItemsByProductId(1L, PageRequest.of(0, 10));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(productRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findByProductId(1L, PageRequest.of(0, 10));
    }

    @Test
    void updateItem_Success() {
        // Arrange
        ItemDTO updateDTO = ItemDTO.builder()
                .productId(1L)
                .quantity(200)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(itemRepository.findByIdAndProductId(1L, 1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // Act
        ItemDTO result = itemService.updateItem(1L, 1L, updateDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(productRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findByIdAndProductId(1L, 1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void deleteItem_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(itemRepository.findByIdAndProductId(1L, 1L)).thenReturn(Optional.of(item));

        // Act
        itemService.deleteItem(1L, 1L);

        // Assert
        verify(productRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findByIdAndProductId(1L, 1L);
        verify(itemRepository, times(1)).delete(any(Item.class));
    }

    @Test
    void deleteAllItemsByProductId_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        itemService.deleteAllItemsByProductId(1L);

        // Assert
        verify(productRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).deleteByProductId(1L);
    }
}
