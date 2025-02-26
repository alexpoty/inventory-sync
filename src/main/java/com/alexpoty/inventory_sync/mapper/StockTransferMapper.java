package com.alexpoty.inventory_sync.mapper;

import com.alexpoty.inventory_sync.dto.transfer.StockTransferResponse;
import com.alexpoty.inventory_sync.model.ProductWarehouse;
import org.mapstruct.Mapper;

@Mapper
public interface StockTransferMapper {

    StockTransferResponse toResponse(ProductWarehouse productWarehouse);
}
