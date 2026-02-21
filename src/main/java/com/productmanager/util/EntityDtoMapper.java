package com.productmanager.util;

import com.productmanager.dto.ProductDTO;
import com.productmanager.dto.ItemDTO;
import com.productmanager.entity.Product;
import com.productmanager.entity.Item;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityDtoMapper {

    /**
     * Convert Product entity to ProductDTO
     */
    public ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }

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

    /**
     * Convert ProductDTO to Product entity
     */
    public Product toProductEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        return Product.builder()
                .id(dto.getId())
                .productName(dto.getProductName())
                .createdBy(dto.getCreatedBy())
                .createdOn(dto.getCreatedOn())
                .modifiedBy(dto.getModifiedBy())
                .modifiedOn(dto.getModifiedOn())
                .build();
    }

    /**
     * Convert Item entity to ItemDTO
     */
    public ItemDTO toItemDTO(Item item) {
        if (item == null) {
            return null;
        }

        return ItemDTO.builder()
                .id(item.getId())
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .quantity(item.getQuantity())
                .build();
    }

    /**
     * Convert ItemDTO to Item entity
     */
    public Item toItemEntity(ItemDTO dto) {
        if (dto == null) {
            return null;
        }

        return Item.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .build();
    }

    /**
     * Convert list of Product entities to list of ProductDTOs
     */
    public List<ProductDTO> toProductDTOList(List<Product> products) {
        if (products == null) {
            return null;
        }

        return products.stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert list of Item entities to list of ItemDTOs
     */
    public List<ItemDTO> toItemDTOList(List<Item> items) {
        if (items == null) {
            return null;
        }

        return items.stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
    }
}
