package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.CreateOrderRequest;
import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.service.OrderService;
import com.coplaca.apirest.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.ORDERS)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<SuccessResponse<OrderDTO>> createOrder(
            Authentication authentication,
            @RequestBody CreateOrderRequest orderRequest) {
        OrderDTO createdOrder = orderService.createOrder(authentication.getName(), orderRequest);
        return ResponseHelper.created(createdOrder, "Order created successfully");
    }

    @GetMapping(ApiConstants.CURRENT_USER)
    @PreAuthorize("hasAnyRole('CUSTOMER', 'DELIVERY')")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getMyOrders(Authentication authentication) {
        return ResponseHelper.ok(orderService.getCurrentUserOrders(authentication.getName()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse<OrderDTO>> getOrder(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getOrderById(id, authentication.getName()));
    }

    @GetMapping("/{id}/eta")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuccessResponse<OrderDTO>> getOrderETA(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getOrderById(id, authentication.getName()));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getCustomerOrders(
            @PathVariable Long customerId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getOrdersByCustomer(customerId, authentication.getName()));
    }

    @GetMapping("/warehouse/{warehouseId}/pending")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getWarehousePendingOrders(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getPendingOrdersByWarehouse(warehouseId, authentication.getName()));
    }

    @GetMapping("/warehouse/{warehouseId}/confirmed")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getWarehouseConfirmedOrders(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getConfirmedOrdersByWarehouse(warehouseId, authentication.getName()));
    }

    @GetMapping("/warehouse/{warehouseId}/in-transit")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getWarehouseInTransitOrders(
            @PathVariable Long warehouseId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getInTransitOrdersByWarehouse(warehouseId, authentication.getName()));
    }

    @PutMapping("/{orderId}/assign/{deliveryAgentId}")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<SuccessResponse<OrderDTO>> assignOrderToDeliveryAgent(
            @PathVariable Long orderId,
            @PathVariable Long deliveryAgentId,
            Authentication authentication) {
        OrderDTO order = orderService.assignOrderToDeliveryAgent(orderId, deliveryAgentId, authentication.getName());
        return ResponseHelper.ok(order, "Order assigned to delivery agent");
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    public ResponseEntity<SuccessResponse<OrderDTO>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status,
            Authentication authentication) {
        OrderDTO order = orderService.updateOrderStatus(orderId, status, authentication.getName());
        return ResponseHelper.ok(order, "Order status updated");
    }

    @GetMapping("/delivery-agent/{deliveryAgentId}")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> getDeliveryAgentOrders(
            @PathVariable Long deliveryAgentId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.getOrdersByDeliveryAgent(deliveryAgentId, authentication.getName()));
    }

    @PutMapping("/{orderId}/accept")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<SuccessResponse<OrderDTO>> acceptOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.acceptOrder(orderId, authentication.getName()), "Order accepted");
    }

    @PutMapping("/{orderId}/reject")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<SuccessResponse<OrderDTO>> rejectOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String reason,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.rejectOrder(orderId, reason, authentication.getName()), "Order rejected");
    }

    @PutMapping("/{orderId}/confirm-loaded")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<SuccessResponse<OrderDTO>> confirmOrderLoaded(
            @PathVariable Long orderId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.confirmOrderLoaded(orderId, authentication.getName()), "Order confirmed as loaded");
    }

    @PutMapping("/{orderId}/deliver")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<SuccessResponse<OrderDTO>> deliverOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.deliverOrder(orderId, authentication.getName()), "Order delivered successfully");
    }

    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<SuccessResponse<OrderDTO>> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String reason,
            Authentication authentication) {
        return ResponseHelper.ok(orderService.cancelOrder(orderId, reason, authentication.getName()), "Order cancelled");
    }
}