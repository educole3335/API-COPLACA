package com.coplaca.apirest.service;

import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    void getWarehouseByIdThrowsWhenNotFound() {
        when(warehouseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> warehouseService.getWarehouseById(99L));
    }

    @Test
    void findNearestWarehouseReturnsClosestWarehouse() {
        Warehouse near = warehouse(1L, 28.4600, -16.2500);
        Warehouse far = warehouse(2L, 28.1000, -15.4000);
        when(warehouseRepository.findByIsActiveTrue()).thenReturn(List.of(near, far));

        Warehouse result = warehouseService.findNearestWarehouse(28.4636, -16.2518);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void assignWarehouseFallsBackToFirstWhenAddressMissing() {
        Warehouse fallback = warehouse(7L, 28.3, -16.4);
        when(warehouseRepository.findFirstByOrderByIdAsc()).thenReturn(fallback);

        Warehouse result = warehouseService.assignWarehouse(null);

        assertEquals(7L, result.getId());
    }

    @Test
    void calculateDistanceKmReturnsPositiveValueForDifferentPoints() {
        double distance = warehouseService.calculateDistanceKm(28.4636, -16.2518, 28.0997, -15.4134);
        assertEquals(true, distance > 0.0d);
    }

    private Warehouse warehouse(Long id, double lat, double lon) {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        warehouse.setName("W-" + id);
        warehouse.setAddress("Address " + id);
        warehouse.setLatitude(lat);
        warehouse.setLongitude(lon);
        warehouse.setActive(true);
        return warehouse;
    }
}
