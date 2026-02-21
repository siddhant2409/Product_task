package com.productmanager.service;

import com.productmanager.dto.ItemDTO;
import com.productmanager.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    ItemDTO createItem(Long productId, ItemDTO itemDTO);

    ItemDTO getItemById(Long productId, Long itemId);

    PagedResponse<ItemDTO> getItemsByProductId(Long productId, Pageable pageable);

    ItemDTO updateItem(Long productId, Long itemId, ItemDTO itemDTO);

    void deleteItem(Long productId, Long itemId);

    void deleteAllItemsByProductId(Long productId);
}
