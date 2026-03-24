package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.CreateOrderRequest;
import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderDTO> createOrder(Authentication authentication,
                                                @RequestBody CreateOrderRequest orderRequest) {
        OrderDTO createdOrder = orderService.createOrder(authentication.getName(), orderRequest);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/payment-methods")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<String>> getCheckoutPaymentMethods() {
        return ResponseEntity.ok(orderService.getCheckoutPaymentMethods());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'DELIVERY')")
    public ResponseEntity<List<OrderDTO>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getCurrentUserOrders(authentication.getName()));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id, Authentication authentication) {
        OrderDTO order = orderService.getOrderById(id, authentication.getName());
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getCustomerOrders(@PathVariable Long customerId,
                                                            Authentication authentication) {
        List<OrderDTO> orders = orderService.getOrdersByCustomer(customerId, authentication.getName());
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/warehouse/{warehouseId}/pending")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getWarehousePendingOrders(@PathVariable Long warehouseId,
                                                                    Authentication authentication) {
        List<OrderDTO> orders = orderService.getPendingOrdersByWarehouse(warehouseId, authentication.getName());
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/{orderId}/assign/{deliveryAgentId}")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<OrderDTO> assignOrderToDeliveryAgent(
            @PathVariable Long orderId,
            @PathVariable Long deliveryAgentId,
            Authentication authentication) {
        OrderDTO order = orderService.assignOrderToDeliveryAgent(orderId, deliveryAgentId, authentication.getName());
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status,
            Authentication authentication) {
        OrderDTO order = orderService.updateOrderStatus(orderId, status, authentication.getName());
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/delivery-agent/{deliveryAgentId}")
    @PreAuthorize("hasAnyRole('DELIVERY', 'LOGISTICS', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getDeliveryAgentOrders(@PathVariable Long deliveryAgentId,
                                                                 Authentication authentication) {
        List<OrderDTO> orders = orderService.getOrdersByDeliveryAgent(deliveryAgentId, authentication.getName());
        return ResponseEntity.ok(orders);
    }

    /**
     * Repartidor acepta una orden asignada
     */
    @PutMapping("/{orderId}/accept")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<OrderDTO> acceptOrder(@PathVariable Long orderId,
                                               Authentication authentication) {
        OrderDTO order = orderService.acceptOrder(orderId, authentication.getName());
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Repartidor rechaza una orden
     */
    @PutMapping("/{orderId}/reject")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<OrderDTO> rejectOrder(@PathVariable Long orderId,
                                               @RequestParam(required = false) String reason,
                                               Authentication authentication) {
        OrderDTO order = orderService.rejectOrder(orderId, reason, authentication.getName());
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Confirma que el camión está cargado y listo para partir
     */
    @PutMapping("/{orderId}/confirm-loaded")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<OrderDTO> confirmOrderLoaded(@PathVariable Long orderId,
                                                       Authentication authentication) {
        OrderDTO order = orderService.confirmOrderLoaded(orderId, authentication.getName());
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Marca una orden como entregada
     */
    @PutMapping("/{orderId}/deliver")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<OrderDTO> deliverOrder(@PathVariable Long orderId,
                                                 Authentication authentication) {
        OrderDTO order = orderService.deliverOrder(orderId, authentication.getName());
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Cancela una orden (solo si está en estado permitido)
     */
    @PutMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId,
                                               @RequestParam(required = false) String reason,
                                               Authentication authentication) {
        OrderDTO order = orderService.cancelOrder(orderId, reason, authentication.getName());
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtiene órdenes de un almacén en estado CONFIRMED (listas para asignar)
     */
    @GetMapping("/warehouse/{warehouseId}/confirmed")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getWarehouseConfirmedOrders(@PathVariable Long warehouseId,
                                                                      Authentication authentication) {
        List<OrderDTO> orders = orderService.getConfirmedOrdersByWarehouse(warehouseId, authentication.getName());
        return ResponseEntity.ok(orders);
    }

    /**
     * Obtiene órdenes en tránsito del almacén
     */
    @GetMapping("/warehouse/{warehouseId}/in-transit")
    @PreAuthorize("hasAnyRole('LOGISTICS', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getWarehouseInTransitOrders(@PathVariable Long warehouseId,
                                                                      Authentication authentication) {
        List<OrderDTO> orders = orderService.getInTransitOrdersByWarehouse(warehouseId, authentication.getName());
        return ResponseEntity.ok(orders);
    }
}
