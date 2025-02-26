package com.alexpoty.inventory_sync.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_warehouse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductWarehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    private Integer quantity;
}
