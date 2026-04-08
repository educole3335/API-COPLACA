package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.CreateOrderItemRequest;
import com.coplaca.apirest.dto.CreateOrderRequest;
import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.DeliveryAgentStatus;
import com.coplaca.apirest.entity.Order;
import com.coplaca.apirest.entity.OrderItem;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.mapper.OrderMapper;
import com.coplaca.apirest.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private static final BigDecimal FREE_DELIVERY_THRESHOLD = new BigDecimal("35.00");
    private static final BigDecimal STANDARD_DELIVERY_FEE = new BigDecimal("4.99");
    private static final List<OrderStatus> ACTIVE_DELIVERY_STATUSES = List.of(OrderStatus.ASSIGNED, OrderStatus.ACCEPTED, OrderStatus.IN_TRANSIT);

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final AddressService addressService;
    private final WarehouseService warehouseService;
    private final OrderMapper orderMapper;

    public OrderDTO createOrder(String customerEmail, CreateOrderRequest request) {
        User customer = getActiveUserByEmail(customerEmail);
        requireRole(customer, "ROLE_CUSTOMER");

        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Orders must include at least one product");
        }

        Address deliveryAddress = resolveDeliveryAddress(customer, request.getDeliveryAddressId());
        Warehouse warehouse = customer.getWarehouse() != null
                ? customer.getWarehouse()
                : warehouseService.assignWarehouse(deliveryAddress);
        customer.setWarehouse(warehouse);

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomer(customer);
        order.setWarehouse(warehouse);
        order.setDeliveryAddress(deliveryAddress);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setPaymentMethod(normalizeValue(request.getPaymentMethod(), "CARD"));
        order.setPaymentStatus(normalizeValue(request.getPaymentStatus(), "PENDING"));

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CreateOrderItemRequest itemRequest : request.getItems()) {
            Product product = productService.getProductEntityById(itemRequest.getProductId());

            if (!product.isActive()) {
                throw new IllegalArgumentException("Product " + product.getName() + " is not available");
            }

            BigDecimal quantity = normalizeQuantity(itemRequest.getQuantity());
            if (product.getStockQuantity().compareTo(quantity) < 0) {
                throw new IllegalArgumentException("Insufficient stock for product " + product.getName());
            }

            BigDecimal lineSubtotal = product.getUnitPrice().multiply(quantity).setScale(2, RoundingMode.HALF_UP);
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setUnitPrice(product.getUnitPrice());
            item.setSubtotal(lineSubtotal);
            order.getItems().add(item);

            product.setStockQuantity(product.getStockQuantity().subtract(quantity));
            productService.saveProduct(product);
            subtotal = subtotal.add(lineSubtotal);
        }

        order.setSubtotal(subtotal);
        order.setDiscount(BigDecimal.ZERO);
        order.setDeliveryFee(subtotal.compareTo(FREE_DELIVERY_THRESHOLD) >= 0 ? BigDecimal.ZERO : STANDARD_DELIVERY_FEE);
        order.setTotalPrice(subtotal.add(order.getDeliveryFee()));
        order.setStatus("COMPLETED".equals(order.getPaymentStatus()) ? OrderStatus.CONFIRMED : OrderStatus.PENDING);
        order.setEstimatedDeliveryTime(calculateEstimatedDeliveryTime(warehouse, deliveryAddress, null));

        return convertToDTO(orderRepository.save(order));
    }

    public OrderDTO getOrderById(Long id, String requesterEmail) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        validateOrderAccess(getActiveUserByEmail(requesterEmail), order);
        return convertToDTO(order);
    }

    public List<OrderDTO> getCurrentUserOrders(String requesterEmail) {
        User user = getActiveUserByEmail(requesterEmail);
        List<Order> orders;

        if (hasRole(user, "ROLE_CUSTOMER")) {
            orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc(user.getId());
        } else if (hasRole(user, "ROLE_DELIVERY")) {
            orders = orderRepository.findByDeliveryAgentIdOrderByCreatedAtDesc(user.getId());
        } else {
            throw new IllegalArgumentException("Current endpoint is available only for customer and delivery users");
        }

        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByCustomer(Long customerId, String requesterEmail) {
        User requester = getActiveUserByEmail(requesterEmail);
        if (!hasRole(requester, "ROLE_ADMIN") && !requester.getId().equals(customerId)) {
            throw new IllegalArgumentException("You can only access your own orders");
        }
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getPendingOrdersByWarehouse(Long warehouseId, String requesterEmail) {
        User requester = getActiveUserByEmail(requesterEmail);
        validateWarehouseAccess(requester, warehouseId);

        return orderRepository.findByWarehouseIdAndStatusInOrderByCreatedAtAsc(
                        warehouseId,
                        List.of(OrderStatus.PENDING))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getAllOrdersByWarehouse(Long warehouseId, String requesterEmail) {
        User requester = getActiveUserByEmail(requesterEmail);
        validateWarehouseAccess(requester, warehouseId);

        return orderRepository.findAll().stream()
                .filter(order -> order.getWarehouse() != null && warehouseId.equals(order.getWarehouse().getId()))
                .sorted((left, right) -> {
                    LocalDateTime leftCreatedAt = left.getCreatedAt() == null ? LocalDateTime.MIN : left.getCreatedAt();
                    LocalDateTime rightCreatedAt = right.getCreatedAt() == null ? LocalDateTime.MIN : right.getCreatedAt();
                    return rightCreatedAt.compareTo(leftCreatedAt);
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO assignOrderToDeliveryAgent(Long orderId, Long deliveryAgentId, String requesterEmail) {
        User requester = getActiveUserByEmail(requesterEmail);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        validateWarehouseAccess(requester, order.getWarehouse().getId());

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("Only confirmed orders can be assigned to delivery agents");
        }

        User deliveryAgent = userService.getUserEntityById(deliveryAgentId);
        requireRole(deliveryAgent, "ROLE_DELIVERY");

        if (deliveryAgent.getWarehouse() == null || !deliveryAgent.getWarehouse().getId().equals(order.getWarehouse().getId())) {
            throw new IllegalArgumentException("Delivery agent must belong to the same warehouse as the order");
        }

        if (deliveryAgent.getDeliveryStatus() != DeliveryAgentStatus.AT_WAREHOUSE) {
            throw new IllegalArgumentException("Delivery agent is not currently available in the warehouse");
        }

        order.setDeliveryAgent(deliveryAgent);
        order.setStatus(OrderStatus.ASSIGNED);
        order.setEstimatedDeliveryTime(calculateEstimatedDeliveryTime(order.getWarehouse(), order.getDeliveryAddress(), deliveryAgent));
        order.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(orderRepository.save(order));
    }

    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status, String requesterEmail) {
        User requester = getActiveUserByEmail(requesterEmail);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (hasRole(requester, "ROLE_DELIVERY")) {
            if (order.getDeliveryAgent() == null || !order.getDeliveryAgent().getId().equals(requester.getId())) {
                throw new IllegalArgumentException("Delivery users can only update their assigned orders");
            }
            applyDeliveryStatusTransition(order, requester, status);
        } else if (hasRole(requester, "ROLE_LOGISTICS") || hasRole(requester, "ROLE_ADMIN")) {
            if (hasRole(requester, "ROLE_LOGISTICS")) {
                validateWarehouseAccess(requester, order.getWarehouse().getId());
            }
            if (status != OrderStatus.CANCELLED && status != OrderStatus.CONFIRMED) {
                throw new IllegalArgumentException("Logistics and admin users can only confirm or cancel orders here");
            }
            order.setStatus(status);
        } else {
            throw new IllegalArgumentException("Current user cannot update order statuses");
        }

        order.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(orderRepository.save(order));
    }

    public List<OrderDTO> getOrdersByDeliveryAgent(Long deliveryAgentId, String requesterEmail) {
        User requester = getActiveUserByEmail(requesterEmail);
        if (hasRole(requester, "ROLE_DELIVERY") && !requester.getId().equals(deliveryAgentId)) {
            throw new IllegalArgumentException("Delivery users can only access their own orders");
        }

        if (hasRole(requester, "ROLE_LOGISTICS")) {
            User deliveryAgent = userService.getUserEntityById(deliveryAgentId);
            validateWarehouseAccess(requester, deliveryAgent.getWarehouse().getId());
        }

        return orderRepository.findByDeliveryAgentIdOrderByCreatedAtDesc(deliveryAgentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<String> getTopProductsSince(LocalDateTime since) {
        List<Order> orders = orderRepository.findByCreatedAtAfter(since);
        Map<String, BigDecimal> counts = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getName(),
                        Collectors.reducing(BigDecimal.ZERO, OrderItem::getQuantity, BigDecimal::add)
                ));

        return counts.entrySet().stream()
                .sorted((left, right) -> right.getValue().compareTo(left.getValue()))
                .map(Map.Entry::getKey)
                .limit(10)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getTopProductsDetailedSince(LocalDateTime since) {
        List<Order> orders = orderRepository.findByCreatedAtAfter(since);
        Map<String, BigDecimal> counts = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getName(),
                        Collectors.reducing(BigDecimal.ZERO, OrderItem::getQuantity, BigDecimal::add)
                ));

        List<Map<String, Object>> topProducts = counts.entrySet().stream()
                .sorted((left, right) -> right.getValue().compareTo(left.getValue()))
                .limit(10)
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("productId", (long) entry.hashCode());
                    item.put("productName", entry.getKey());
                    item.put("unitsSold", entry.getValue().intValue());
                    return item;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("topProducts", topProducts);
        return result;
    }

    public Map<String, Object> getOrderStatsSince(LocalDateTime since) {
        List<Order> orders = orderRepository.findByCreatedAtAfter(since);

        long totalOrders = orders.size();
        long completedOrders = orders.stream()
                .filter(this::isCompletedOrder)
                .count();

        List<Order> billableOrders = orders.stream()
                .filter(this::isBillableOrder)
                .toList();

        BigDecimal revenue = billableOrders.stream()
                .map(order -> order.getTotalPrice() == null ? BigDecimal.ZERO : order.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal averageOrderValue = billableOrders.isEmpty()
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : revenue.divide(BigDecimal.valueOf(billableOrders.size()), 2, RoundingMode.HALF_UP);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("completedOrders", completedOrders);
        stats.put("averageOrderValue", averageOrderValue);
        stats.put("revenue", revenue);
        return stats;
    }

    public void validateUserCanBeDisabled(Long userId) {
        User user = userService.getUserEntityById(userId);

        if (hasRole(user, "ROLE_CUSTOMER")) {
            boolean hasPendingOrders = orderRepository.findByCustomerId(userId).stream()
                    .anyMatch(this::isOpenOrder);
            if (hasPendingOrders) {
                throw new IllegalStateException("No se puede eliminar el usuario porque tiene pedidos pendientes");
            }
        }

        if (hasRole(user, "ROLE_DELIVERY")) {
            boolean hasPendingOrders = orderRepository.findByDeliveryAgentId(userId).stream()
                    .anyMatch(this::isOpenOrder);
            if (hasPendingOrders) {
                throw new IllegalStateException("No se puede eliminar el repartidor porque tiene pedidos pendientes");
            }
        }
    }

    public List<OrderDTO> getOrdersToday() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        return orderRepository.findByCreatedAtAfter(startOfDay).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

        public Map<String, Object> getWarehouseStats(Long warehouseId, String requesterEmail, LocalDateTime since) {
        User requester = getActiveUserByEmail(requesterEmail);
        validateWarehouseAccess(requester, warehouseId);

        List<Order> warehouseOrders = orderRepository.findAll().stream()
            .filter(order -> order.getWarehouse() != null && warehouseId.equals(order.getWarehouse().getId()))
            .filter(order -> order.getCreatedAt() != null && order.getCreatedAt().isAfter(since))
            .toList();

        long totalOrders = warehouseOrders.size();
        long deliveredOrders = warehouseOrders.stream().filter(order -> order.getStatus() == OrderStatus.DELIVERED).count();
        long pendingOrders = warehouseOrders.stream().filter(order -> order.getStatus() == OrderStatus.PENDING).count();
        long confirmedOrders = warehouseOrders.stream().filter(order -> order.getStatus() == OrderStatus.CONFIRMED).count();
        long inTransitOrders = warehouseOrders.stream().filter(order -> order.getStatus() == OrderStatus.IN_TRANSIT).count();
        long cancelledOrders = warehouseOrders.stream().filter(order -> order.getStatus() == OrderStatus.CANCELLED).count();
        long completedOrders = warehouseOrders.stream().filter(this::isCompletedOrder).count();

        BigDecimal revenue = warehouseOrders.stream()
            .filter(this::isBillableOrder)
            .map(order -> order.getTotalPrice() == null ? BigDecimal.ZERO : order.getTotalPrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal pendingRevenue = warehouseOrders.stream()
            .filter(order -> order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.CONFIRMED)
            .map(order -> order.getTotalPrice() == null ? BigDecimal.ZERO : order.getTotalPrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);

        BigDecimal averageOrderValue = totalOrders == 0
            ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
            : revenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);

        List<UserDTO> users = userService.getAllUsers();
        long activeLogisticsUsers = users.stream()
            .filter(UserDTO::isEnabled)
            .filter(user -> warehouseId.equals(user.getWarehouseId()))
            .filter(user -> user.getRoles().stream().anyMatch(role -> "LOGISTICS".equalsIgnoreCase(role)))
            .count();
        long activeDeliveryUsers = users.stream()
            .filter(UserDTO::isEnabled)
            .filter(user -> warehouseId.equals(user.getWarehouseId()))
            .filter(user -> user.getRoles().stream().anyMatch(role -> "DELIVERY".equalsIgnoreCase(role)))
            .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("completedOrders", completedOrders);
        stats.put("pendingOrders", pendingOrders);
        stats.put("confirmedOrders", confirmedOrders);
        stats.put("inTransitOrders", inTransitOrders);
        stats.put("deliveredOrders", deliveredOrders);
        stats.put("cancelledOrders", cancelledOrders);
        stats.put("revenue", revenue);
        stats.put("pendingRevenue", pendingRevenue);
        stats.put("averageOrderValue", averageOrderValue);
        stats.put("activeLogisticsUsers", activeLogisticsUsers);
        stats.put("activeDeliveryUsers", activeDeliveryUsers);
        return stats;
        }

    private boolean isCompletedOrder(Order order) {
        if (order == null) {
            return false;
        }
        return order.getStatus() == OrderStatus.DELIVERED
                || "COMPLETED".equalsIgnoreCase(order.getPaymentStatus());
    }

    private boolean isOpenOrder(Order order) {
        if (order == null || order.getStatus() == null) {
            return false;
        }
        return order.getStatus() != OrderStatus.DELIVERED && order.getStatus() != OrderStatus.CANCELLED;
    }

    private boolean isBillableOrder(Order order) {
        if (order == null) {
            return false;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return false;
        }

        return order.getTotalPrice() != null
                && order.getTotalPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    private void applyDeliveryStatusTransition(Order order, User deliveryAgent, OrderStatus newStatus) {
        if (newStatus == OrderStatus.ACCEPTED && order.getStatus() == OrderStatus.ASSIGNED) {
            order.setStatus(OrderStatus.ACCEPTED);
            return;
        }

        if (newStatus == OrderStatus.IN_TRANSIT && order.getStatus() == OrderStatus.ACCEPTED) {
            deliveryAgent.setDeliveryStatus(DeliveryAgentStatus.DELIVERING);
            order.setStatus(OrderStatus.IN_TRANSIT);
            order.setEstimatedDeliveryTime(calculateEstimatedDeliveryTime(order.getWarehouse(), order.getDeliveryAddress(), deliveryAgent));
            return;
        }

        if (newStatus == OrderStatus.DELIVERED && order.getStatus() == OrderStatus.IN_TRANSIT) {
            deliveryAgent.setDeliveryStatus(DeliveryAgentStatus.AT_WAREHOUSE);
            order.setStatus(OrderStatus.DELIVERED);
            order.setActualDeliveryTime(LocalDateTime.now());
            return;
        }

        throw new IllegalArgumentException("Invalid delivery status transition");
    }

    private void validateOrderAccess(User requester, Order order) {
        if (hasRole(requester, "ROLE_ADMIN")) {
            return;
        }

        if (hasRole(requester, "ROLE_CUSTOMER") && order.getCustomer().getId().equals(requester.getId())) {
            return;
        }

        if (hasRole(requester, "ROLE_DELIVERY") && order.getDeliveryAgent() != null
                && order.getDeliveryAgent().getId().equals(requester.getId())) {
            return;
        }

        if (hasRole(requester, "ROLE_LOGISTICS") && requester.getWarehouse() != null
                && requester.getWarehouse().getId().equals(order.getWarehouse().getId())) {
            return;
        }

        throw new IllegalArgumentException("You do not have access to this order");
    }

    private void validateWarehouseAccess(User requester, Long warehouseId) {
        if (hasRole(requester, "ROLE_ADMIN")) {
            return;
        }

        if (!hasRole(requester, "ROLE_LOGISTICS")) {
            throw new IllegalArgumentException("Only logistics and admin users can access warehouse operations");
        }

        if (requester.getWarehouse() == null || !requester.getWarehouse().getId().equals(warehouseId)) {
            throw new IllegalArgumentException("You can only manage orders from your own warehouse");
        }
    }

    private Address resolveDeliveryAddress(User customer, Long deliveryAddressId) {
        if (deliveryAddressId == null) {
            if (customer.getAddress() == null) {
                throw new IllegalArgumentException("Customer does not have a delivery address configured");
            }
            return customer.getAddress();
        }

        Address address = addressService.getAddressById(deliveryAddressId);
        if (customer.getAddress() == null || !customer.getAddress().getId().equals(address.getId())) {
            throw new IllegalArgumentException("Orders can only be delivered to the customer's registered address");
        }
        return address;
    }

    private User getActiveUserByEmail(String email) {
        return userService.getUserEntityByEmail(email);
    }

    private void requireRole(User user, String roleName) {
        if (!hasRole(user, roleName)) {
            throw new IllegalArgumentException("User does not have the required role: " + roleName);
        }
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(roleName));
    }

    private BigDecimal normalizeQuantity(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Item quantity must be greater than zero");
        }
        return quantity.setScale(3, RoundingMode.HALF_UP);
    }

    private String normalizeValue(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value.trim().toUpperCase();
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private LocalDateTime calculateEstimatedDeliveryTime(Warehouse warehouse, Address deliveryAddress, User deliveryAgent) {
        double distanceKm = warehouseService.calculateDistanceKm(
                warehouse.getLatitude(),
                warehouse.getLongitude(),
                deliveryAddress.getLatitude(),
                deliveryAddress.getLongitude());
        long travelMinutes = Math.max(20L, Math.round((distanceKm / 35.0d) * 60.0d));
        long activeStops = deliveryAgent == null ? 0L : orderRepository.countByDeliveryAgentIdAndStatusIn(deliveryAgent.getId(), ACTIVE_DELIVERY_STATUSES);
        long stopBuffer = Math.max(0L, activeStops) * 12L;
        return LocalDateTime.now().plusMinutes(30L + travelMinutes + stopBuffer);
    }

    private OrderDTO convertToDTO(Order order) {
        return orderMapper.toDTO(order);
    }

    /**
     * Repartidor acepta una orden asignada
     */
    public OrderDTO acceptOrder(Long orderId, String requesterEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        User requester = getActiveUserByEmail(requesterEmail);
        requireRole(requester, "ROLE_DELIVERY");
        
        if (order.getDeliveryAgent() == null || !order.getDeliveryAgent().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("You can only accept orders assigned to you");
        }
        
        if (order.getStatus() != OrderStatus.ASSIGNED) {
            throw new IllegalArgumentException("Only ASSIGNED orders can be accepted");
        }
        
        order.setStatus(OrderStatus.ACCEPTED);
        order.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(orderRepository.save(order));
    }

    /**
     * Repartidor rechaza una orden
     */
    public OrderDTO rejectOrder(Long orderId, String reason, String requesterEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        User requester = getActiveUserByEmail(requesterEmail);
        requireRole(requester, "ROLE_DELIVERY");
        
        if (order.getDeliveryAgent() == null || !order.getDeliveryAgent().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("You can only reject orders assigned to you");
        }
        
        if (order.getStatus() != OrderStatus.ASSIGNED && order.getStatus() != OrderStatus.ACCEPTED) {
            throw new IllegalArgumentException("Only ASSIGNED or ACCEPTED orders can be rejected");
        }
        
        // Volver a estado CONFIRMED para que logística reasigne
        order.setStatus(OrderStatus.CONFIRMED);
        order.setDeliveryAgent(null);
        order.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(orderRepository.save(order));
    }

    /**
     * Confirma que el camión está cargado y listo para partir
     */
    public OrderDTO confirmOrderLoaded(Long orderId, String requesterEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        User requester = getActiveUserByEmail(requesterEmail);
        requireRole(requester, "ROLE_DELIVERY");
        
        if (order.getDeliveryAgent() == null || !order.getDeliveryAgent().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("You can only confirm loading for your assigned orders");
        }
        
        if (order.getStatus() != OrderStatus.ACCEPTED) {
            throw new IllegalArgumentException("Only ACCEPTED orders can be confirmed as loaded");
        }
        
        order.setStatus(OrderStatus.IN_TRANSIT);
        requester.setDeliveryStatus(DeliveryAgentStatus.DELIVERING);
        order.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(orderRepository.save(order));
    }

    /**
     * Marca una orden como entregada
     */
    public OrderDTO deliverOrder(Long orderId, String requesterEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        User requester = getActiveUserByEmail(requesterEmail);
        requireRole(requester, "ROLE_DELIVERY");
        
        if (order.getDeliveryAgent() == null || !order.getDeliveryAgent().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("You can only deliver orders assigned to you");
        }
        
        if (order.getStatus() != OrderStatus.IN_TRANSIT) {
            throw new IllegalArgumentException("Only IN_TRANSIT orders can be marked as delivered");
        }
        
        order.setStatus(OrderStatus.DELIVERED);
        order.setActualDeliveryTime(LocalDateTime.now());
        requester.setDeliveryStatus(DeliveryAgentStatus.AT_WAREHOUSE);
        order.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(orderRepository.save(order));
    }

    /**
     * Cancela una orden
     */
    public OrderDTO cancelOrder(Long orderId, String reason, String requesterEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        
        User requester = getActiveUserByEmail(requesterEmail);
        
        // Clientes solo pueden cancelar sus propios pedidos en ciertos estados
        if (hasRole(requester, "ROLE_CUSTOMER")) {
            if (!order.getCustomer().getId().equals(requester.getId())) {
                throw new IllegalArgumentException("Customers can only cancel their own orders");
            }
            if (!List.of(OrderStatus.PENDING, OrderStatus.CONFIRMED).contains(order.getStatus())) {
                throw new IllegalArgumentException("Customers can only cancel PENDING or CONFIRMED orders");
            }
        } else if (!hasRole(requester, "ROLE_ADMIN")) {
            throw new IllegalArgumentException("You don't have permission to cancel orders");
        }
        
        // Restaurar stock si no salió de almacén
        if (!List.of(OrderStatus.IN_TRANSIT, OrderStatus.DELIVERED).contains(order.getStatus())) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStockQuantity(product.getStockQuantity().add(item.getQuantity()));
                productService.saveProduct(product);
            }
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(orderRepository.save(order));
    }

    /**
     * Obtiene órdenes confirmadas de un almacén (listas para asignar)
     */
    public List<OrderDTO> getConfirmedOrdersByWarehouse(Long warehouseId, String requesterEmail) {
        User requester = getActiveUserByEmail(requesterEmail);
        
        if (hasRole(requester, "ROLE_LOGISTICS")) {
            validateWarehouseAccess(requester, warehouseId);
        } else if (!hasRole(requester, "ROLE_ADMIN")) {
            throw new IllegalArgumentException("Only logistics and admin users can access this");
        }
        
        return orderRepository.findAll().stream()
                .filter(o -> o.getWarehouse().getId().equals(warehouseId))
                .filter(o -> o.getStatus() == OrderStatus.CONFIRMED)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene órdenes en tránsito de un almacén
     */
    public List<OrderDTO> getInTransitOrdersByWarehouse(Long warehouseId, String requesterEmail) {
        User requester = getActiveUserByEmail(requesterEmail);
        // TODO. Usar enum
        if (hasRole(requester, "ROLE_LOGISTICS")) {
            validateWarehouseAccess(requester, warehouseId);
        } else if (!hasRole(requester, "ROLE_ADMIN")) {
            throw new IllegalArgumentException("Only logistics and admin users can access this");
        }
        
        return orderRepository.findAll().stream()
                .filter(o -> o.getWarehouse().getId().equals(warehouseId))
                .filter(o -> o.getStatus() == OrderStatus.IN_TRANSIT)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
