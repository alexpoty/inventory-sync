package com.alexpoty.inventory_sync.service;

import com.alexpoty.inventory_sync.dto.supplier.SupplierRequest;
import com.alexpoty.inventory_sync.dto.supplier.SupplierResponse;
import com.alexpoty.inventory_sync.exception.supplier.SupplierNotFoundException;
import com.alexpoty.inventory_sync.model.Supplier;
import com.alexpoty.inventory_sync.repository.SupplierRepository;
import com.alexpoty.inventory_sync.service.impl.SupplierServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void should_throw_when_updating_not_existing_supplier() {
        // when
        when(repository.existsById(anyLong())).thenReturn(false);
        // assert
        assertThrows(SupplierNotFoundException.class, () -> service.updateSupplier(1L, createSupplierRequest()));
    }

    @Test
    public void should_get_a_list_of_suppliers() {
        // when
        when(repository.findAll()).thenReturn(List.of(createSupplier()));
        List<SupplierResponse> actual = service.getAllSuppliers();
        // assert
        assertEquals(1, actual.size());
        assertEquals("Test", actual.getFirst().name());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void should_find_a_supplier_by_id() {
        // when
        when(repository.findById(anyLong())).thenReturn(Optional.of(createSupplier()));
        SupplierResponse actual = service.getSupplier(1L);
        // assert
        assertNotNull(actual);
        assertEquals("Test", actual.name());
        assertEquals("Test", actual.contactInfo());
        assertEquals(1L, actual.id());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void should_throw_SupplierNotFoundException() {
        // when
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        // assert
        assertThrows(SupplierNotFoundException.class, () -> service.getSupplier(1L));
    }

    @Test
    public void should_delete_a_supplier() {
        // when
        doNothing().when(repository).deleteById(anyLong());
        when(repository.existsById(anyLong())).thenReturn(true);
        service.deleteSupplier(1L);
        // assert
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    public void should_throw_when_deleting_supplier() {
        // when
        when(repository.existsById(anyLong())).thenReturn(false);
        // assert
        assertThrows(SupplierNotFoundException.class, () -> service.deleteSupplier(1L));
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
}