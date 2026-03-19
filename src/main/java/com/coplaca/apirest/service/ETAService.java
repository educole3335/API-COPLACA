package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.ETAResponseDTO;
import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.Order;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para cálculo de ETA (Estimated Time of Arrival)
 * Calcula el tiempo estimado de entrega considerando:
 * - Distancia entre almacén y domicilio
 * - Paradas intermedias (otras entregas en ruta)
 * - Condiciones de la ruta
 */
@Service
@Transactional
public class ETAService {

    private final GeolocationService geolocationService;
    private final OrderRepository orderRepository;

    public ETAService(GeolocationService geolocationService, OrderRepository orderRepository) {
        this.geolocationService = geolocationService;
        this.orderRepository = orderRepository;
    }

    /**
     * Calcula el ETA para un pedido
     */
    public ETAResponseDTO calculateETA(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        Address warehouseAddress = order.getWarehouse().getAddress();
        Address deliveryAddress = order.getDeliveryAddress();

        if (warehouseAddress == null || deliveryAddress == null) {
            return buildDefaultETA(order);
        }

        if (!geolocationService.areCoordinatesValid(warehouseAddress.getLatitude(), warehouseAddress.getLongitude()) ||
                !geolocationService.areCoordinatesValid(deliveryAddress.getLatitude(), deliveryAddress.getLongitude())) {
            return buildDefaultETA(order);
        }

        // Calcular distancia
        BigDecimal distanceKm = geolocationService.calculateDistance(
                warehouseAddress.getLatitude(),
                warehouseAddress.getLongitude(),
                deliveryAddress.getLatitude(),
                deliveryAddress.getLongitude()
        );

        // Contar paradas adicionales en la ruta (otros pedidos del mismo repartidor)
        Integer additionalStops = countAdditionalStopsOnRoute(order);

        // Calcular minutos de entrega
        Integer estimatedMinutes = geolocationService.calculateEstimatedMinutes(distanceKm, additionalStops);

        // Actualizar la orden con tiempo estimado
        LocalDateTime estimatedTime = LocalDateTime.now().plusMinutes(estimatedMinutes);
        order.setEstimatedDeliveryTime(estimatedTime);
        orderRepository.save(order);

        return ETAResponseDTO.builder()
                .orderId(orderId)
                .estimatedDeliveryTime(estimatedTime)
                .estimatedMinutes(estimatedMinutes)
                .distanceKm(distanceKm)
                .status(order.getStatus().toString())
                .totalStopsOnRoute(additionalStops)
                .build();
    }

    /**
     * Recalcula ETA para todos los pedidos en tránsito del repartidor
     */
    public void recalculateETAForDeliveryAgent(Long deliveryAgentId) {
        List<Order> activeOrders = orderRepository.findAll().stream()
                .filter(o -> o.getDeliveryAgent() != null &&
                        o.getDeliveryAgent().getId().equals(deliveryAgentId) &&
                        (o.getStatus() == OrderStatus.IN_TRANSIT || o.getStatus() == OrderStatus.ASSIGNED))
                .toList();

        for (Order order : activeOrders) {
            calculateETA(order.getId());
        }
    }

    /**
     * Cuenta paradas adicionales del repartidor en la ruta
     */
    private Integer countAdditionalStopsOnRoute(Order currentOrder) {
        if (currentOrder.getDeliveryAgent() == null) {
            return 0;
        }

        return (int) orderRepository.findAll().stream()
                .filter(o -> o.getDeliveryAgent() != null &&
                        o.getDeliveryAgent().getId().equals(currentOrder.getDeliveryAgent().getId()) &&
                        (o.getStatus() == OrderStatus.IN_TRANSIT || o.getStatus() == OrderStatus.ASSIGNED) &&
                        !o.getId().equals(currentOrder.getId()))
                .count();
    }

    /**
     * Construye un ETA por defecto (30 minutos) cuando no hay datos de ubicación
     */
    private ETAResponseDTO buildDefaultETA(Order order) {
        LocalDateTime estimatedTime = LocalDateTime.now().plusMinutes(30);
        orderRepository.save(order);

        return ETAResponseDTO.builder()
                .orderId(order.getId())
                .estimatedDeliveryTime(estimatedTime)
                .estimatedMinutes(30)
                .distanceKm(BigDecimal.ZERO)
                .status(order.getStatus().toString())
                .totalStopsOnRoute(0)
                .build();
    }

    /**
     * Obtiene el ETA más reciente de una orden
     */
    public ETAResponseDTO getLatestETA(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getEstimatedDeliveryTime() != null) {
            int minutes = (int) java.time.temporal.ChronoUnit.MINUTES.between(
                    LocalDateTime.now(),
                    order.getEstimatedDeliveryTime()
            );

            return ETAResponseDTO.builder()
                    .orderId(orderId)
                    .estimatedDeliveryTime(order.getEstimatedDeliveryTime())
                    .estimatedMinutes(Math.max(minutes, 0))
                    .status(order.getStatus().toString())
                    .build();
        }

        return calculateETA(orderId);
    }
}
