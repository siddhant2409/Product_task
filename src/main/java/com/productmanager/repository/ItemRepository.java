package com.productmanager.repository;

import com.productmanager.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByProductId(Long productId, Pageable pageable);

    List<Item> findByProductId(Long productId);

    Optional<Item> findByIdAndProductId(Long itemId, Long productId);

    long countByProductId(Long productId);

    void deleteByProductId(Long productId);
}
