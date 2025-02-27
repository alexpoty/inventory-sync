package com.alexpoty.inventory_sync.mapper;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;
import com.alexpoty.inventory_sync.model.Product;
import com.alexpoty.inventory_sync.model.ProductWarehouse;
import com.alexpoty.inventory_sync.model.Warehouse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTransferMapperTest {

    private final StockTransferMapper mapper = new StockTransferMapperImpl();

    @Test
    public void should_convert_model_to_response() {
        //given
        ProductWarehouse productWarehouse = ProductWarehouse.builder()
                .id(1L)
                .product(Product.builder().name("Test").build())
                .warehouse(Warehouse.builder().name("Test").build())
                .quantity(200)
                .build();
        // when
        StockTransferResponse actual = mapper.toResponse(productWarehouse);
        // assert
        assertNotNull(actual);
        assertEquals(productWarehouse.getProduct().getName(), actual.product().getName());
        assertEquals(productWarehouse.getId(), actual.id());
        assertEquals(productWarehouse.getQuantity(), actual.quantity());
    }
}