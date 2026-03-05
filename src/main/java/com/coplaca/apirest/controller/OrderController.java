package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.entity.Order;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody Order order) {
        OrderDTO createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getCustomerOrders(@PathVariable Long customerId) {
        List<OrderDTO> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/warehouse/{warehouseId}/pending")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getWarehousePendingOrders(@PathVariable Long warehouseId) {
        List<OrderDTO> orders = orderService.getPendingOrdersByWarehouse(warehouseId);
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/{orderId}/assign/{deliveryAgentId}")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<OrderDTO> assignOrderToDeliveryAgent(
            @PathVariable Long orderId,
            @PathVariable Long deliveryAgentId) {
        OrderDTO order = orderService.assignOrderToDeliveryAgent(orderId, deliveryAgentId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        OrderDTO order = orderService.updateOrderStatus(orderId, status);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/delivery-agent/{deliveryAgentId}")
    @PreAuthorize("hasAnyRole('DELIVERY', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getDeliveryAgentOrders(@PathVariable Long deliveryAgentId) {
        List<OrderDTO> orders = orderService.getOrdersByDeliveryAgent(deliveryAgentId);
        return ResponseEntity.ok(orders);
    }
}
