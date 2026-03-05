package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.dto.OrderItemDTO;
import com.coplaca.apirest.entity.Order;
import com.coplaca.apirest.entity.OrderItem;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.repository.UserRepository;
import com.coplaca.apirest.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    
    public OrderService(OrderRepository orderRepository, 
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }
    
    public OrderDTO createOrder(Order order) {
        // Generate unique order number
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setStatus(OrderStatus.PENDING);
        
        // Calculate totals
        BigDecimal subtotal = order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setSubtotal(subtotal);
        
        BigDecimal total = subtotal;
        if (order.getDeliveryFee() != null) {
            total = total.add(order.getDeliveryFee());
        }
        if (order.getDiscount() != null) {
            total = total.subtract(order.getDiscount());
        }
        
        order.setTotalPrice(total);
        Order savedOrder = orderRepository.save(order);
        
        return convertToDTO(savedOrder);
    }
    
    public OrderDTO getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(this::convertToDTO).orElse(null);
    }
    
    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getPendingOrdersByWarehouse(Long warehouseId) {
        List<Order> orders = orderRepository.findByWarehouseIdAndStatus(warehouseId, OrderStatus.CONFIRMED);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public OrderDTO assignOrderToDeliveryAgent(Long orderId, Long deliveryAgentId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order o = order.get();
            Optional<User> agent = userRepository.findById(deliveryAgentId);
            if (agent.isPresent()) {
                o.setDeliveryAgent(agent.get());
                o.setStatus(OrderStatus.ASSIGNED);
                Order saved = orderRepository.save(o);
                return convertToDTO(saved);
            }
        }
        return null;
    }
    
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order o = order.get();
            o.setStatus(status);
            if (status == OrderStatus.DELIVERED) {
                o.setActualDeliveryTime(LocalDateTime.now());
            }
            Order saved = orderRepository.save(o);
            return convertToDTO(saved);
        }
        return null;
    }
    
    public List<OrderDTO> getOrdersByDeliveryAgent(Long deliveryAgentId) {
        List<Order> orders = orderRepository.findByDeliveryAgentId(deliveryAgentId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<String> getTopProductsSince(java.time.LocalDateTime since) {
        List<Order> orders = orderRepository.findByCreatedAtAfter(since);
        java.util.Map<String, Double> counts = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getName(),
                        Collectors.summingDouble(item -> item.getQuantity())
                ));
        return counts.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .map(e -> e.getKey())
                .limit(10)
                .collect(Collectors.toList());
    }
    
    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomer().getId())
                .warehouseId(order.getWarehouse().getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .subtotal(order.getSubtotal())
                .discount(order.getDiscount())
                .deliveryFee(order.getDeliveryFee())
                .items(order.getItems().stream()
                        .map(item -> OrderItemDTO.builder()
                                .id(item.getId())
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .subtotal(item.getSubtotal())
                                .build())
                        .collect(Collectors.toList()))
                .deliveryAgentId(order.getDeliveryAgent() != null ? order.getDeliveryAgent().getId() : null)
                .deliveryAgentName(order.getDeliveryAgent() != null ? order.getDeliveryAgent().getFirstName() + " " + order.getDeliveryAgent().getLastName() : null)
                .deliveryAddressId(order.getDeliveryAddress() != null ? order.getDeliveryAddress().getId() : null)
                .estimatedDeliveryTime(order.getEstimatedDeliveryTime())
                .actualDeliveryTime(order.getActualDeliveryTime())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
