package com.alexpoty.inventory_sync.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Warehouse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
}
