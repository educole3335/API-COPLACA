package com.coplaca.apirest.repository;

import com.coplaca.apirest.entity.Order;
import com.coplaca.apirest.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Order> findByWarehouseIdAndStatus(Long warehouseId, OrderStatus status);
    List<Order> findByDeliveryAgentId(Long deliveryAgentId);
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByCreatedAtAfter(java.time.LocalDateTime date);
}
