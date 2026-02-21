package com.productmanager.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product", indexes = {
        @Index(name = "idx_product_name", columnList = "product_name"),
        @Index(name = "idx_created_by", columnList = "created_by")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Product Entity")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Product ID", example = "1")
    private Long id;

    @Column(name = "product_name", nullable = false, length = 255)
    @Schema(description = "Product Name", example = "Laptop")
    private String productName;

    @Column(name = "created_by", nullable = false, length = 100)
    @Schema(description = "Created By User", example = "admin")
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    @Schema(description = "Created Timestamp")
    private LocalDateTime createdOn;

    @Column(name = "modified_by", length = 100)
    @Schema(description = "Modified By User", example = "admin")
    private String modifiedBy;

    @UpdateTimestamp
    @Column(name = "modified_on")
    @Schema(description = "Last Modified Timestamp")
    private LocalDateTime modifiedOn;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    @Version
    @Column(name = "version")
    private Long version;

    public void addItem(Item item) {
        item.setProduct(this);
        this.items.add(item);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
        item.setProduct(null);
    }
}
