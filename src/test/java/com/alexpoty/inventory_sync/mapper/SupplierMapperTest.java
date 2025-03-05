package com.alexpoty.inventory_sync.mapper;

import com.alexpoty.inventory_sync.dto.supplier.SupplierRequest;
import com.alexpoty.inventory_sync.dto.supplier.SupplierResponse;
import com.alexpoty.inventory_sync.model.Supplier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierMapperTest {

    private final SupplierMapper mapper = new SupplierMapperImpl();

    @Test
    public void should_convert_from_request_to_model() {
        // given
        SupplierRequest request = new SupplierRequest("Test", "Test");
        // when
        Supplier actual = mapper.toSupplier(request);
        // assert
        assertNotNull(actual);
        assertEquals("Test", actual.getName());
        assertEquals("Test", actual.getContactInfo());
        assertNull(actual.getId());
    }

    @Test
    public void should_convert_from_model_to_response() {
        // given
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test");
        supplier.setContactInfo("Test");
        // when
        SupplierResponse response = mapper.toSupplierResponse(supplier);
        // assert
        assertNotNull(response);
        assertEquals(supplier.getId(), response.id());
        assertEquals(supplier.getName(), response.name());
        assertEquals(supplier.getContactInfo(), response.contactInfo());
    }
}