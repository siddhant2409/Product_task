package com.productmanager.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "item", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_quantity", columnList = "quantity")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Item Entity")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Item ID", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_item_product"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Schema(description = "Associated Product")
    private Product product;

    @Column(name = "quantity", nullable = false)
    @Schema(description = "Item Quantity", example = "100")
    private Integer quantity;

    @Version
    @Column(name = "version")
    private Long version;
}
