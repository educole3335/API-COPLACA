package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.CreateOrderItemRequest;
import com.coplaca.apirest.dto.CreateOrderRequest;
import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.DeliveryAgentStatus;
import com.coplaca.apirest.entity.Order;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.entity.Product;
import com.coplaca.apirest.entity.ProductCategory;
import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.repository.AddressRepository;
import com.coplaca.apirest.repository.OrderRepository;
import com.coplaca.apirest.repository.ProductRepository;
import com.coplaca.apirest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private WarehouseService warehouseService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderCalculatesTotalsAndReducesStock() {
        User customer = customer(1L);
        Product product = product(100L, new BigDecimal("10.000"));

        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(List.of(itemRequest(100L, new BigDecimal("2.500"))));

        when(userRepository.findByEmailAndEnabledTrue("customer@coplaca.com")).thenReturn(Optional.of(customer));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(warehouseService.calculateDistanceKm(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(10.0d);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderDTO dto = orderService.createOrder("customer@coplaca.com", request);

        assertNotNull(dto.getOrderNumber());
        assertEquals(new BigDecimal("6.25"), dto.getSubtotal());
        assertEquals(new BigDecimal("4.99"), dto.getDeliveryFee());
        assertEquals(OrderStatus.PENDING, dto.getStatus());
        assertEquals(new BigDecimal("7.500"), product.getStockQuantity());
    }

    @Test
    void createOrderFailsWhenStockIsInsufficient() {
        User customer = customer(1L);
        Product product = product(100L, new BigDecimal("1.000"));

        CreateOrderRequest request = new CreateOrderRequest();
        request.setItems(List.of(itemRequest(100L, new BigDecimal("2.000"))));

        when(userRepository.findByEmailAndEnabledTrue("customer@coplaca.com")).thenReturn(Optional.of(customer));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder("customer@coplaca.com", request));
    }

    @Test
    void assignOrderToDeliveryAgentRequiresConfirmedOrder() {
        User logistics = logistics(2L, 30L);
        User delivery = delivery(3L, 30L);
        Order order = order(20L, OrderStatus.PENDING, logistics.getWarehouse(), logistics(9L, 30L));

        when(userRepository.findByEmailAndEnabledTrue("logistics@coplaca.com")).thenReturn(Optional.of(logistics));
        when(orderRepository.findById(20L)).thenReturn(Optional.of(order));

        assertThrows(IllegalArgumentException.class,
                () -> orderService.assignOrderToDeliveryAgent(20L, 3L, "logistics@coplaca.com"));
    }

    @Test
    void updateOrderStatusSetsDeliveredAndReturnsAgentToWarehouse() {
        User delivery = delivery(3L, 30L);
        Order order = order(20L, OrderStatus.IN_TRANSIT, delivery.getWarehouse(), customer(1L));
        order.setDeliveryAgent(delivery);

        when(userRepository.findByEmailAndEnabledTrue("delivery@coplaca.com")).thenReturn(Optional.of(delivery));
        when(orderRepository.findById(20L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderDTO updated = orderService.updateOrderStatus(20L, OrderStatus.DELIVERED, "delivery@coplaca.com");

        assertEquals(OrderStatus.DELIVERED, updated.getStatus());
        assertNotNull(updated.getActualDeliveryTime());
        assertEquals(DeliveryAgentStatus.AT_WAREHOUSE, delivery.getDeliveryStatus());
    }

    private CreateOrderItemRequest itemRequest(Long productId, BigDecimal quantity) {
        CreateOrderItemRequest request = new CreateOrderItemRequest();
        request.setProductId(productId);
        request.setQuantity(quantity);
        return request;
    }

    private User customer(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("customer@coplaca.com");
        user.setRoles(Set.of(role("ROLE_CUSTOMER")));
        user.setAddress(address(7L));
        user.setWarehouse(warehouse(30L));
        user.setEnabled(true);
        return user;
    }

    private User logistics(Long id, Long warehouseId) {
        User user = new User();
        user.setId(id);
        user.setEmail("logistics@coplaca.com");
        user.setRoles(Set.of(role("ROLE_LOGISTICS")));
        user.setWarehouse(warehouse(warehouseId));
        user.setEnabled(true);
        return user;
    }

    private User delivery(Long id, Long warehouseId) {
        User user = new User();
        user.setId(id);
        user.setEmail("delivery@coplaca.com");
        user.setRoles(Set.of(role("ROLE_DELIVERY")));
        user.setWarehouse(warehouse(warehouseId));
        user.setDeliveryStatus(DeliveryAgentStatus.AT_WAREHOUSE);
        user.setEnabled(true);
        return user;
    }

    private Order order(Long id, OrderStatus status, Warehouse warehouse, User customer) {
        Order order = new Order();
        order.setId(id);
        order.setStatus(status);
        order.setWarehouse(warehouse);
        order.setCustomer(customer);
        order.setDeliveryAddress(address(11L));
        return order;
    }

    private Product product(Long id, BigDecimal stock) {
        ProductCategory category = new ProductCategory();
        category.setId(10L);
        category.setName("Fruta");

        Product product = new Product();
        product.setId(id);
        product.setName("Platano");
        product.setUnit("kg");
        product.setUnitPrice(new BigDecimal("2.50"));
        product.setStockQuantity(stock);
        product.setCategory(category);
        product.setActive(true);
        return product;
    }

    private Role role(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }

    private Warehouse warehouse(Long id) {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        warehouse.setName("WH-" + id);
        warehouse.setAddress("Address");
        warehouse.setLatitude(28.4);
        warehouse.setLongitude(-16.2);
        return warehouse;
    }

    private Address address(Long id) {
        Address address = new Address();
        address.setId(id);
        address.setStreet("Street");
        address.setCity("City");
        address.setPostalCode("38001");
        address.setProvince("Province");
        address.setLatitude(28.4);
        address.setLongitude(-16.2);
        return address;
    }
}
