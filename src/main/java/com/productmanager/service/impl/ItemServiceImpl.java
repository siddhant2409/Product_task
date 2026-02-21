package com.productmanager.service.impl;

import com.productmanager.dto.ItemDTO;
import com.productmanager.dto.PagedResponse;
import com.productmanager.entity.Item;
import com.productmanager.entity.Product;
import com.productmanager.exception.ResourceNotFoundException;
import com.productmanager.repository.ItemRepository;
import com.productmanager.repository.ProductRepository;
import com.productmanager.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@SuppressWarnings("null")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ItemDTO createItem(Long productId, ItemDTO itemDTO) {
        log.info("Creating item for product id: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(productId));

        Item item = Item.builder()
                .product(product)
                .quantity(itemDTO.getQuantity())
                .build();

        Item savedItem = itemRepository.save(item);
        log.info("Item created successfully with id: {}", savedItem.getId());

        return mapToDTO(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDTO getItemById(Long productId, Long itemId) {
        log.info("Fetching item with id: {} for product id: {}", itemId, productId);

        // Verify product exists
        productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(productId));

        Item item = itemRepository.findByIdAndProductId(itemId, productId)
                .orElseThrow(() -> ResourceNotFoundException.itemNotFound(itemId));

        return mapToDTO(item);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ItemDTO> getItemsByProductId(Long productId, Pageable pageable) {
        log.info("Fetching items for product id: {}", productId);

        // Verify product exists
        productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(productId));

        Page<Item> page = itemRepository.findByProductId(productId, pageable);
        return mapPageToResponse(page);
    }

    @Override
    public ItemDTO updateItem(Long productId, Long itemId, ItemDTO itemDTO) {
        log.info("Updating item with id: {} for product id: {}", itemId, productId);

        // Verify product exists
        productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(productId));

        Item item = itemRepository.findByIdAndProductId(itemId, productId)
                .orElseThrow(() -> ResourceNotFoundException.itemNotFound(itemId));

        item.setQuantity(itemDTO.getQuantity());

        Item updatedItem = itemRepository.save(item);
        log.info("Item updated successfully with id: {}", itemId);

        return mapToDTO(updatedItem);
    }

    @Override
    public void deleteItem(Long productId, Long itemId) {
        log.info("Deleting item with id: {} for product id: {}", itemId, productId);

        // Verify product exists
        productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(productId));

        Item item = itemRepository.findByIdAndProductId(itemId, productId)
                .orElseThrow(() -> ResourceNotFoundException.itemNotFound(itemId));

        itemRepository.delete(item);
        log.info("Item deleted successfully with id: {}", itemId);
    }

    @Override
    public void deleteAllItemsByProductId(Long productId) {
        log.info("Deleting all items for product id: {}", productId);

        // Verify product exists
        productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(productId));

        itemRepository.deleteByProductId(productId);
        log.info("All items deleted for product id: {}", productId);
    }

    private ItemDTO mapToDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .quantity(item.getQuantity())
                .build();
    }

    private PagedResponse<ItemDTO> mapPageToResponse(Page<Item> page) {
        return PagedResponse.<ItemDTO>builder()
                .content(page.getContent().stream().map(this::mapToDTO).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
    }
}
