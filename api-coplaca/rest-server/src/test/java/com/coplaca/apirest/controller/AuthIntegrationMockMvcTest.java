package com.coplaca.apirest.controller;

import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.security.JwtTokenProvider;
import com.coplaca.apirest.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

      @MockitoBean
    private UserService userService;

      @MockitoBean
    private AuthenticationManager authenticationManager;

      @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void loginReturnsTokenWithMockMvc() throws Exception {
        String payload = """
            {
              \"email\": \"user@coplaca.com\",
              \"password\": \"secret\"
            }
            """;

        Authentication authentication = new UsernamePasswordAuthenticationToken("user@coplaca.com", "secret");
        User user = new User();
        user.setId(1L);
        user.setEmail("user@coplaca.com");
        user.setFirstName("Test");
        user.setLastName("User");
        Role role = new Role();
        role.setName("ROLE_CUSTOMER");
        user.setRoles(Set.of(role));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userService.findByEmail("user@coplaca.com")).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.email").value("user@coplaca.com"))
                .andExpect(jsonPath("$.roles[0]").value("CUSTOMER"));
    }

    @Test
    void signupRejectsAdminRoleWithBadRequest() throws Exception {
        String payload = """
                {
                  \"email\": \"new@coplaca.com\",
                  \"password\": \"password\",
                  \"firstName\": \"Nuevo\",
                  \"lastName\": \"Cliente\",
                  \"role\": \"ROLE_ADMIN\",
                  \"address\": {
                    \"street\": \"Calle 1\",
                    \"streetNumber\": \"10\",
                    \"city\": \"Santa Cruz\",
                    \"postalCode\": \"38001\",
                    \"province\": \"SC Tenerife\"
                  }
                }
                """;

        when(userService.emailExists("new@coplaca.com")).thenReturn(false);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Public signup is only available for customer accounts"));
    }
}
