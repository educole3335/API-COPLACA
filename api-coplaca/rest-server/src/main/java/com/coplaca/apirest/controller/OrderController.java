package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.CreateOrderRequest;
import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.service.OrderService;
import com.coplaca.apirest.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.ORDERS)
@Tag(name = "04 - Pedidos y ETA", description = "Creación, seguimiento y gestión del ciclo de vida de pedidos")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Crear pedido", description = "Genera un pedido a partir del carrito del cliente autenticado")
    public ResponseEntity<SuccessResponse<OrderDTO>> createOrder(
            Authentication authentication,
            @RequestBody CreateOrderRequest orderRequest) {
        OrderDTO createdOrder = orderService.createOrder(authentication.getName(), orderRequest);
        return ResponseHelper.created(createdOrder, "Order created successfully");
    }

    @GetMapping(ApiConstants.CURRENT_USER)
    @PreAuthorize("hasAnyRole('CUSTOMER', 'DELIVERY')")
    @Operation(summary = "Pedidos del usuario actual", description = "Devuelve los pedidos del usuario autenticado según su rol")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getMyOrders(Authentication authentication) {
        return ResponseHelper.ok(orderService.getCurrentUserOrders(authentication.getName()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener pedido por ID", description = "Consulta el detalle de un pedido con control de acceso")
    public ResponseEntity<SuccessResponse<OrderDTO>> getOrder(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getOrderById(id, authentication.getName()));
    }

    @GetMapping("/{id}/eta")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener ETA del pedido", description = "Devuelve la información de ETA asociada al pedido")
    public ResponseEntity<SuccessResponse<OrderDTO>> getOrderETA(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getOrderById(id, authentication.getName()));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Pedidos por cliente", description = "Lista los pedidos de un cliente concreto")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getCustomerOrders(
            @PathVariable Long customerId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getOrdersByCustomer(customerId, authentication.getName()));
    }

    @GetMapping("/warehouse/{warehouseId}/pending")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Pedidos pendientes por almacén", description = "Lista pedidos pendientes listos para gestión logística")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getWarehousePendingOrders(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getPendingOrdersByWarehouse(warehouseId, authentication.getName()));
    }

    @GetMapping("/warehouse/{warehouseId}/all")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Todos los pedidos por almacén", description = "Devuelve el histórico completo del almacén")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getWarehouseAllOrders(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getAllOrdersByWarehouse(warehouseId, authentication.getName()));
    }

    @GetMapping("/warehouse/{warehouseId}/confirmed")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Pedidos confirmados por almacén", description = "Lista los pedidos confirmados del almacén")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getWarehouseConfirmedOrders(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getConfirmedOrdersByWarehouse(warehouseId, authentication.getName()));
    }

    @GetMapping("/warehouse/{warehouseId}/ready-for-assignment")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Pedidos listos para asignar", description = "Lista pedidos confirmados preparados para asignar repartidor")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getWarehouseReadyForAssignmentOrders(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getConfirmedOrdersByWarehouse(warehouseId, authentication.getName()));
    }

    @GetMapping("/warehouse/{warehouseId}/in-transit")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Pedidos en tránsito por almacén", description = "Lista los pedidos que ya están en reparto")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getWarehouseInTransitOrders(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getInTransitOrdersByWarehouse(warehouseId, authentication.getName()));
    }

    @GetMapping("/warehouse/{warehouseId}/stats")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Estadísticas por almacén", description = "Obtiene métricas operativas del almacén para el periodo solicitado")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> getWarehouseStats(
            @PathVariable Long warehouseId,
            @RequestParam(required = false) String period,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getWarehouseStats(warehouseId, authentication.getName(), resolveStatsStart(period)));
    }

    @PutMapping("/{orderId}/assign/{deliveryAgentId}")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Asignar pedido a repartidor", description = "Asigna un pedido confirmado a un agente de reparto")
    public ResponseEntity<SuccessResponse<OrderDTO>> assignOrderToDeliveryAgent(
            @PathVariable Long orderId,
            @PathVariable Long deliveryAgentId,
            Authentication authentication) {
        OrderDTO order = orderService.assignOrderToDeliveryAgent(orderId, deliveryAgentId, authentication.getName());
        return ResponseHelper.ok(order, "Order assigned to delivery agent");
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    @Operation(summary = "Actualizar estado del pedido", description = "Cambia el estado operativo del pedido según el rol del usuario")
    public ResponseEntity<SuccessResponse<OrderDTO>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status,
            Authentication authentication) {
        OrderDTO order = orderService.updateOrderStatus(orderId, status, authentication.getName());
        return ResponseHelper.ok(order, "Order status updated");
    }

    @PutMapping("/{orderId}/confirm")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    @Operation(summary = "Confirmar pedido", description = "Permite a logística confirmar pedidos pendientes")
    public ResponseEntity<SuccessResponse<OrderDTO>> confirmOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        OrderDTO order = orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED, authentication.getName());
        return ResponseHelper.ok(order, "Order confirmed");
    }

    @GetMapping("/delivery-agent/{deliveryAgentId}")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    @Operation(summary = "Pedidos por repartidor", description = "Devuelve los pedidos asignados a un agente de reparto")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getDeliveryAgentOrders(
            @PathVariable Long deliveryAgentId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getOrdersByDeliveryAgent(deliveryAgentId, authentication.getName()));
    }

    @PutMapping("/{orderId}/accept")
    @PreAuthorize("hasRole('DELIVERY')")
    @Operation(summary = "Aceptar pedido", description = "El repartidor acepta un pedido asignado")
    public ResponseEntity<SuccessResponse<OrderDTO>> acceptOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.acceptOrder(orderId, authentication.getName()), "Order accepted");
    }

    @PutMapping("/{orderId}/reject")
    @PreAuthorize("hasRole('DELIVERY')")
    @Operation(summary = "Rechazar pedido", description = "El repartidor rechaza un pedido asignado")
    public ResponseEntity<SuccessResponse<OrderDTO>> rejectOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String reason,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.rejectOrder(orderId, reason, authentication.getName()), "Order rejected");
    }

    @PutMapping("/{orderId}/confirm-loaded")
    @PreAuthorize("hasRole('DELIVERY')")
    @Operation(summary = "Confirmar carga", description = "Marca el pedido como cargado y listo para salir")
    public ResponseEntity<SuccessResponse<OrderDTO>> confirmOrderLoaded(
            @PathVariable Long orderId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.confirmOrderLoaded(orderId, authentication.getName()), "Order confirmed as loaded");
    }

    @PutMapping("/{orderId}/deliver")
    @PreAuthorize("hasRole('DELIVERY')")
    @Operation(summary = "Marcar pedido como entregado", description = "Finaliza el ciclo del pedido para el repartidor")
    public ResponseEntity<SuccessResponse<OrderDTO>> deliverOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.deliverOrder(orderId, authentication.getName()), "Order delivered successfully");
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Cancelar pedido", description = "Cancela un pedido según las reglas de negocio y permisos")
    public ResponseEntity<SuccessResponse<OrderDTO>> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String reason,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.cancelOrder(orderId, reason, authentication.getName()), "Order cancelled");
    }

    private LocalDateTime resolveStatsStart(String period) {
        String normalized = period == null ? "month" : period.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "day", "today" -> LocalDateTime.now().minus(1, ChronoUnit.DAYS);
            case "week", "7d" -> LocalDateTime.now().minus(7, ChronoUnit.DAYS);
            case "month", "30d", "" -> LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
            default -> LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        };
    }
}