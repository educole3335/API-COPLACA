package com.coplaca.apirest.order.controller;

import com.coplaca.apirest.dto.CreateOrderRequest;
import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.service.OrderService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order-domain")
public class OrderDomainController {

    private final OrderService orderService;

    public OrderDomainController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestParam String customerEmail,
                                                @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(customerEmail, request));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id,
                                                 @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.getOrderById(id, requesterEmail));
    }

    @GetMapping("/orders/my")
    public ResponseEntity<List<OrderDTO>> getCurrentUserOrders(@RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.getCurrentUserOrders(requesterEmail));
    }

    @GetMapping("/orders/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable Long customerId,
                                                              @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId, requesterEmail));
    }

    @GetMapping("/orders/warehouse/{warehouseId}/pending")
    public ResponseEntity<List<OrderDTO>> getPendingOrdersByWarehouse(@PathVariable Long warehouseId,
                                                                       @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.getPendingOrdersByWarehouse(warehouseId, requesterEmail));
    }

    @PutMapping("/orders/{orderId}/assign/{deliveryAgentId}")
    public ResponseEntity<OrderDTO> assignOrderToDeliveryAgent(@PathVariable Long orderId,
                                                                @PathVariable Long deliveryAgentId,
                                                                @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.assignOrderToDeliveryAgent(orderId, deliveryAgentId, requesterEmail));
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId,
                                                       @RequestParam OrderStatus status,
                                                       @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status, requesterEmail));
    }

    @GetMapping("/orders/delivery-agent/{deliveryAgentId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByDeliveryAgent(@PathVariable Long deliveryAgentId,
                                                                    @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.getOrdersByDeliveryAgent(deliveryAgentId, requesterEmail));
    }

    @PutMapping("/orders/{orderId}/accept")
    public ResponseEntity<OrderDTO> acceptOrder(@PathVariable Long orderId,
                                                 @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.acceptOrder(orderId, requesterEmail));
    }

    @PutMapping("/orders/{orderId}/reject")
    public ResponseEntity<OrderDTO> rejectOrder(@PathVariable Long orderId,
                                                 @RequestParam(required = false) String reason,
                                                 @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.rejectOrder(orderId, reason, requesterEmail));
    }

    @PutMapping("/orders/{orderId}/confirm-loaded")
    public ResponseEntity<OrderDTO> confirmOrderLoaded(@PathVariable Long orderId,
                                                        @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.confirmOrderLoaded(orderId, requesterEmail));
    }

    @PutMapping("/orders/{orderId}/deliver")
    public ResponseEntity<OrderDTO> deliverOrder(@PathVariable Long orderId,
                                                  @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.deliverOrder(orderId, requesterEmail));
    }

    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId,
                                                 @RequestParam(required = false) String reason,
                                                 @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, reason, requesterEmail));
    }

    @GetMapping("/orders/warehouse/{warehouseId}/confirmed")
    public ResponseEntity<List<OrderDTO>> getConfirmedOrdersByWarehouse(@PathVariable Long warehouseId,
                                                                         @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.getConfirmedOrdersByWarehouse(warehouseId, requesterEmail));
    }

    @GetMapping("/orders/warehouse/{warehouseId}/in-transit")
    public ResponseEntity<List<OrderDTO>> getInTransitOrdersByWarehouse(@PathVariable Long warehouseId,
                                                                         @RequestParam String requesterEmail) {
        return ResponseEntity.ok(orderService.getInTransitOrdersByWarehouse(warehouseId, requesterEmail));
    }
}
