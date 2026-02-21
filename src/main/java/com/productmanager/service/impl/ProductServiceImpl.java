package com.productmanager.service.impl;

import com.productmanager.dto.ProductDTO;
import com.productmanager.dto.PagedResponse;
import com.productmanager.entity.Product;
import com.productmanager.exception.DuplicateResourceException;
import com.productmanager.exception.ResourceNotFoundException;
import com.productmanager.repository.ProductRepository;
import com.productmanager.service.ProductService;
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
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO, String username) {
        log.info("Creating product with name: {}", productDTO.getProductName());

        // Check if product name already exists
        if (productRepository.countByProductNameIgnoreCase(productDTO.getProductName()) > 0) {
            throw DuplicateResourceException.productNameExists(productDTO.getProductName());
        }

        Product product = Product.builder()
                .productName(productDTO.getProductName())
                .createdBy(username)
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with id: {}", savedProduct.getId());

        return mapToDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findByIdWithItems(id)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(id));

        return mapToDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductDTO> getAllProducts(Pageable pageable) {
        log.info("Fetching all products with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> page = productRepository.findAll(pageable);
        return mapPageToResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductDTO> searchProducts(String keyword, Pageable pageable) {
        log.info("Searching products with keyword: {}", keyword);
        Page<Product> page = productRepository.searchProducts(keyword, pageable);
        return mapPageToResponse(page);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, String username) {
        log.info("Updating product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(id));

        // Check if new product name is already taken by another product
        if (!product.getProductName().equalsIgnoreCase(productDTO.getProductName())) {
            if (productRepository.countByProductNameIgnoreCase(productDTO.getProductName()) > 0) {
                throw DuplicateResourceException.productNameExists(productDTO.getProductName());
            }
        }

        product.setProductName(productDTO.getProductName());
        product.setModifiedBy(username);

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with id: {}", id);

        return mapToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.productNotFound(id));

        productRepository.delete(product);
        log.info("Product deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductDTO> getProductsByCreatedBy(String createdBy, Pageable pageable) {
        log.info("Fetching products created by: {}", createdBy);
        Page<Product> page = productRepository.findByCreatedBy(createdBy, pageable);
        return mapPageToResponse(page);
    }

    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .createdBy(product.getCreatedBy())
                .createdOn(product.getCreatedOn())
                .modifiedBy(product.getModifiedBy())
                .modifiedOn(product.getModifiedOn())
                .itemCount(product.getItems() != null ? product.getItems().size() : 0)
                .build();
    }

    private PagedResponse<ProductDTO> mapPageToResponse(Page<Product> page) {
        return PagedResponse.<ProductDTO>builder()
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
