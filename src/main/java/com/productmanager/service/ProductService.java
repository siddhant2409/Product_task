package com.productmanager.service;

import com.productmanager.dto.ProductDTO;
import com.productmanager.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO, String username);

    ProductDTO getProductById(Long id);

    PagedResponse<ProductDTO> getAllProducts(Pageable pageable);

    PagedResponse<ProductDTO> searchProducts(String keyword, Pageable pageable);

    ProductDTO updateProduct(Long id, ProductDTO productDTO, String username);

    void deleteProduct(Long id);

    PagedResponse<ProductDTO> getProductsByCreatedBy(String createdBy, Pageable pageable);
}
