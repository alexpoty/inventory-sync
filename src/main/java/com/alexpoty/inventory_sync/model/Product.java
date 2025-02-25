package com.alexpoty.inventory_sync.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "warehouses_id")
    @JsonBackReference
    private Warehouse warehouse;
    private Instant created_at;
    private Instant updated_at;
}
