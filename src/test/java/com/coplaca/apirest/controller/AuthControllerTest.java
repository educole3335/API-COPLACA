package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.AddressDTO;
import com.coplaca.apirest.dto.LoginRequest;
import com.coplaca.apirest.dto.LoginResponse;
import com.coplaca.apirest.dto.SignUpRequest;
import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.entity.Warehouse;
import com.coplaca.apirest.repository.RoleRepository;
import com.coplaca.apirest.security.JwtTokenProvider;
import com.coplaca.apirest.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginReturnsTokenWhenCredentialsAreValid() {
        LoginRequest request = LoginRequest.builder()
                .email("user@coplaca.com")
                .password("secret")
                .build();

        User user = user("user@coplaca.com", "ROLE_CUSTOMER");
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@coplaca.com", "secret");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userService.findByEmail("user@coplaca.com")).thenReturn(Optional.of(user));
        when(tokenProvider.generateToken(authentication)).thenReturn("jwt-token");

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token", response.getBody().getToken());
    }

    @Test
    void signupRejectsNonCustomerRole() {
        SignUpRequest request = signupRequest();
        request.setRole("ROLE_ADMIN");

        when(userService.emailExists(request.getEmail())).thenReturn(false);

        ResponseEntity<LoginResponse> response = authController.signup(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void signupCreatesCustomerAndReturnsToken() {
        SignUpRequest request = signupRequest();

        Role role = new Role();
        role.setName("ROLE_CUSTOMER");

        User savedUser = user("new@coplaca.com", "ROLE_CUSTOMER");
        savedUser.setId(5L);

        Warehouse warehouse = new Warehouse();
        warehouse.setId(11L);

        when(userService.emailExists(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_CUSTOMER")).thenReturn(Optional.of(role));
        when(userService.resolveCustomerWarehouse(any())).thenReturn(warehouse);
        when(userService.createUser(any(User.class))).thenReturn(savedUser);
        when(tokenProvider.generateTokenFromEmail("new@coplaca.com")).thenReturn("jwt-signup");

        ResponseEntity<LoginResponse> response = authController.signup(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-signup", response.getBody().getToken());
        assertEquals(5L, response.getBody().getId());
    }

    private User user(String email, String roleName) {
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setFirstName("Test");
        user.setLastName("User");

        Role role = new Role();
        role.setName(roleName);
        user.setRoles(Set.of(role));
        return user;
    }

    private SignUpRequest signupRequest() {
        AddressDTO address = AddressDTO.builder()
                .street("Calle 1")
                .streetNumber("10")
                .city("Santa Cruz")
                .postalCode("38001")
                .province("SC Tenerife")
                .latitude(28.46)
                .longitude(-16.25)
                .build();

        SignUpRequest request = new SignUpRequest();
        request.setEmail("new@coplaca.com");
        request.setPassword("password");
        request.setFirstName("Nuevo");
        request.setLastName("Cliente");
        request.setAddress(address);
        return request;
    }
}
