package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.service.UserService;
import com.coplaca.apirest.service.WarehouseService;
import com.coplaca.apirest.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.WAREHOUSES)
@Tag(name = "05 - Almacenes", description = "Consulta y administración de almacenes y repartidores disponibles")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Listar almacenes", description = "Devuelve todos los almacenes activos")
    public ResponseEntity<SuccessResponse<List<Warehouse>>> getAll() {
        return ResponseHelper.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener almacén por ID", description = "Recupera el detalle de un almacén")
    public ResponseEntity<SuccessResponse<Warehouse>> getById(@PathVariable Long id) {
        return ResponseHelper.ok(warehouseService.getWarehouseById(id));
    }

    @GetMapping("/{id}/delivery-agents")
    @PreAuthorize("hasAnyRole('LOGISTICS','ADMIN')")
    @Operation(summary = "Repartidores disponibles por almacén", description = "Lista los repartidores disponibles para asignación")
    public ResponseEntity<SuccessResponse<List<UserDTO>>> getAvailableDeliveryAgents(@PathVariable Long id) {
        return ResponseHelper.ok(userService.getAvailableDeliveryAgents(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear almacén", description = "Crea un nuevo almacén desde administración")
    public ResponseEntity<SuccessResponse<Warehouse>> create(@RequestBody Warehouse warehouse) {
        return ResponseHelper.created(warehouseService.createWarehouse(warehouse), "Warehouse created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar almacén", description = "Actualiza los datos de un almacén existente")
    public ResponseEntity<SuccessResponse<Warehouse>> update(
            @PathVariable Long id,
            @RequestBody Warehouse warehouseDetails) {
        return ResponseHelper.ok(warehouseService.updateWarehouse(id, warehouseDetails), "Warehouse updated successfully");
    }
}