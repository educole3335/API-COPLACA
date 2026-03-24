package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.service.UserService;
import com.coplaca.apirest.service.WarehouseService;
import com.coplaca.apirest.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.WAREHOUSES)
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Warehouse>>> getAll() {
        return ResponseHelper.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Warehouse>> getById(@PathVariable Long id) {
        return ResponseHelper.ok(warehouseService.getWarehouseById(id));
    }

    @GetMapping("/{id}/delivery-agents")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    public ResponseEntity<SuccessResponse<List<UserDTO>>> getAvailableDeliveryAgents(@PathVariable Long id) {
        return ResponseHelper.ok(userService.getAvailableDeliveryAgents(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Warehouse>> create(@RequestBody Warehouse warehouse) {
        return ResponseHelper.created(warehouseService.createWarehouse(warehouse), "Warehouse created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Warehouse>> update(
            @PathVariable Long id,
            @RequestBody Warehouse warehouseDetails) {
        return ResponseHelper.ok(warehouseService.updateWarehouse(id, warehouseDetails), "Warehouse updated successfully");
    }
}