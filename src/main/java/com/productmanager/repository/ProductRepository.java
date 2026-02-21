package com.productmanager.repository;

import com.productmanager.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductName(String productName);

    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    Page<Product> findByCreatedBy(String createdBy, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productName LIKE CONCAT('%', :keyword, '%') OR p.createdBy LIKE CONCAT('%', :keyword, '%')")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE LOWER(p.productName) = LOWER(:productName)")
    long countByProductNameIgnoreCase(@Param("productName") String productName);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.items WHERE p.id = :id")
    Optional<Product> findByIdWithItems(@Param("id") Long id);

    List<Product> findByCreatedByOrderByCreatedOnDesc(String createdBy);
}
