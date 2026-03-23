package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.entity.OrderStatus;
import com.coplaca.apirest.service.OrderService;
import com.coplaca.apirest.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderAndRoleAuthorizationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private UserService userService;

    @Test
    void customerCanCreateOrder() throws Exception {
        String payload = """
                {
                  \"deliveryAddressId\": 1,
                  \"paymentMethod\": \"CARD\",
                  \"paymentStatus\": \"PENDING\",
                  \"items\": [
                    {
                      \"productId\": 7,
                      \"quantity\": 2
                    }
                  ]
                }
                """;

        OrderDTO created = OrderDTO.builder()
                .id(101L)
                .orderNumber("ORD-101")
                .status(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("12.50"))
                .build();

        when(orderService.createOrder(eq("customer@coplaca.com"), any())).thenReturn(created);

        mockMvc.perform(post("/orders")
                        .with(user("customer@coplaca.com").roles("CUSTOMER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.orderNumber").value("ORD-101"));
    }

    @Test
    void deliveryRoleCannotCreateCustomerOrder() throws Exception {
        String payload = """
                {
                  \"deliveryAddressId\": 1,
                  \"paymentMethod\": \"CARD\",
                  \"paymentStatus\": \"PENDING\",
                  \"items\": [
                    {
                      \"productId\": 7,
                      \"quantity\": 2
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/orders")
                        .with(user("delivery@coplaca.com").roles("DELIVERY"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpointRequiresAdminRole() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(user("customer@coplaca.com").roles("CUSTOMER")))
                .andExpect(status().isForbidden());

        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/admin/users")
                        .with(user("admin@coplaca.com").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
