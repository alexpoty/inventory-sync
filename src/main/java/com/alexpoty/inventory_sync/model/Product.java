package com.alexpoty.inventory_sync.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    @Positive
    private BigDecimal price;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "warehouses_id")
    private Warehouse warehouse;
    private Instant created_at;
    private Instant updated_at;
}
