package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.service.UserService;
import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Warehouse>> getAll() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getById(@PathVariable Long id) {
        Warehouse w = warehouseService.getWarehouseById(id);
        if (w != null) {
            return ResponseEntity.ok(w);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/delivery-agents")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<List<UserDTO>> getAvailableDeliveryAgents(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAvailableDeliveryAgents(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Warehouse> create(@RequestBody Warehouse warehouse) {
        Warehouse created = warehouseService.createWarehouse(warehouse);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Warehouse> update(@PathVariable Long id,
                                            @RequestBody Warehouse warehouseDetails) {
        Warehouse updated = warehouseService.updateWarehouse(id, warehouseDetails);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
}