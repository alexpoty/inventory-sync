package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.supplier.SupplierRequest;
import com.alexpoty.inventory_sync.dto.supplier.SupplierResponse;
import com.alexpoty.inventory_sync.model.Supplier;
import com.alexpoty.inventory_sync.repository.SupplierRepository;
import com.alexpoty.inventory_sync.service.impl.SupplierServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository repository;
    @InjectMocks
    private SupplierServiceImpl service;

    @Test
    public void should_create_supplier() {
        //when
        when(repository.save(any(Supplier.class))).thenReturn(createSupplier());
        SupplierResponse actual = service.createSupplier(createSupplierRequest());
        // assert
        assertNotNull(actual);
        assertEquals("Test", actual.name());
        assertEquals("Test", actual.contactInfo());
        verify(repository, times(1)).save(any(Supplier.class));
    }

    @Test
    public void should_update_supplier() {
        // when
        when(repository.save(any(Supplier.class))).thenReturn(createSupplier());
        when(repository.existsById(any(Long.class))).thenReturn(true);
        SupplierResponse actual = service.updateSupplier(1L, createSupplierRequest());
        // assert
        assertNotNull(actual);
        assertEquals("Test", actual.name());
        assertEquals("Test", actual.contactInfo());
        verify(repository, times(1)).save(any(Supplier.class));
        verify(repository, times(1)).existsById(anyLong());
    }

    private Supplier createSupplier() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("Test");
        supplier.setContactInfo("Test");
        return supplier;
    }

    private SupplierRequest createSupplierRequest() {
        return new SupplierRequest("Test", "Test");
    }

    private SupplierResponse createSupplierResponse() {
        return new SupplierResponse(1L, "Test", "Test");
    }
}