package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.LoginRequest;
import com.coplaca.apirest.dto.LoginResponse;
import com.coplaca.apirest.dto.SignUpRequest;
import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.exception.ResourceNotFoundException;
import com.coplaca.apirest.repository.RoleRepository;
import com.coplaca.apirest.security.JwtTokenProvider;
import com.coplaca.apirest.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "01 - Autenticación", description = "Inicio de sesión y registro público de clientes")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
        @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un JWT junto con sus datos básicos")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación correcta"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
        })
        public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Credenciales de acceso") @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

        String token = tokenProvider.generateToken(authentication);
        LoginResponse response = LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", ""))
                        .collect(Collectors.toSet()))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
        @Operation(summary = "Registrar cliente", description = "Crea una cuenta pública de cliente con dirección obligatoria")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro correcto"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "409", description = "El email ya existe")
        })
        public ResponseEntity<LoginResponse> signup(
            @Parameter(description = "Datos del nuevo cliente") @RequestBody SignUpRequest signUpRequest) {
        if (userService.emailExists(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("A user with that email already exists");
        }

        if (signUpRequest.getAddress() == null) {
            throw new IllegalArgumentException("Customers must register with a delivery address");
        }

        String requestedRole = signUpRequest.getRole();
        if (requestedRole != null && !requestedRole.isBlank() && !"ROLE_CUSTOMER".equalsIgnoreCase(requestedRole)
                && !"CUSTOMER".equalsIgnoreCase(requestedRole)) {
            throw new IllegalArgumentException("Public signup is only available for customer accounts");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());

        Address address = new Address();
        address.setStreet(signUpRequest.getAddress().getStreet());
        address.setStreetNumber(signUpRequest.getAddress().getStreetNumber());
        address.setApartment(signUpRequest.getAddress().getApartment());
        address.setCity(signUpRequest.getAddress().getCity());
        address.setPostalCode(signUpRequest.getAddress().getPostalCode());
        address.setProvince(signUpRequest.getAddress().getProvince());
        address.setLatitude(signUpRequest.getAddress().getLatitude());
        address.setLongitude(signUpRequest.getAddress().getLongitude());
        address.setAdditionalInfo(signUpRequest.getAddress().getAdditionalInfo());
        user.setAddress(address);

        Optional<Role> role = roleRepository.findByName("ROLE_CUSTOMER");
        Set<Role> roles = new HashSet<>();
        role.ifPresent(roles::add);
        user.setRoles(roles);
        user.setWarehouse(userService.resolveCustomerWarehouse(user.getAddress()));

        User savedUser = userService.createUser(user);
        String token = tokenProvider.generateTokenFromEmail(savedUser.getEmail());

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .roles(savedUser.getRoles().stream()
                        .map(r -> r.getName().replace("ROLE_", ""))
                        .collect(Collectors.toSet()))
                .build();

        return ResponseEntity.ok(response);
    }
}
