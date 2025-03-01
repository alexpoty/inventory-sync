package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.model.ProductWarehouse;
import com.alexpoty.inventory_sync.repository.ProductWarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StockReplenishJob {

    private final ProductWarehouseRepository productWarehouseRepository;

    @Scheduled(cron = "0 0 09 * * MON-FRI")
    @Async
    public void checkStockForLowQuantity() {
        //TODO Check quantity for products if product is low stock then notify owner that product is low
        List<ProductWarehouse> products = productWarehouseRepository.findAll();
        Map<String, String> order = new HashMap<>();
        products.stream().filter(request -> request.getQuantity() <= 10)
                .forEach(product -> order.put(product.getProduct().getName(), product.getWarehouse().getName()));
        orderLowQuantityOrder(order);
    }

    public void orderLowQuantityOrder(Map<String, String> productsToOrder) {

    }
}
