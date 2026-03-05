package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.LoginRequest;
import com.coplaca.apirest.dto.LoginResponse;
import com.coplaca.apirest.dto.SignUpRequest;
import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.repository.RoleRepository;
import com.coplaca.apirest.service.UserService;
import com.coplaca.apirest.service.WarehouseService;
import com.coplaca.apirest.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final WarehouseService warehouseService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    public AuthController(UserService userService,
                          RoleRepository roleRepository,
                          WarehouseService warehouseService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.warehouseService = warehouseService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            Optional<User> user = userService.findByEmail(loginRequest.getEmail());
            if (user.isPresent()) {
                String token = tokenProvider.generateToken(authentication);
                User u = user.get();
                
                LoginResponse response = LoginResponse.builder()
                        .token(token)
                        .type("Bearer")
                        .id(u.getId())
                        .email(u.getEmail())
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .roles(u.getRoles().stream()
                                .map(role -> role.getName().replace("ROLE_", ""))
                                .collect(Collectors.toSet()))
                        .build();
                
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();
    }
    
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signup(@RequestBody SignUpRequest signUpRequest) {
        try {
            // Check if user already exists
            Optional<User> existingUser = userService.findByEmail(signUpRequest.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create new user
            User user = new User();
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(signUpRequest.getPassword());
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setPhoneNumber(signUpRequest.getPhoneNumber());
            
            // Create address if provided
            if (signUpRequest.getAddress() != null) {
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
                
                // Assign nearest warehouse for customers
                if ("ROLE_CUSTOMER".equals(signUpRequest.getRole())) {
                    user.setWarehouse(warehouseService.findNearestWarehouse(
                            address.getLatitude(),
                            address.getLongitude()
                    ));
                }
            }
            
            // Set role
            String roleName = signUpRequest.getRole() != null ? signUpRequest.getRole() : "ROLE_CUSTOMER";
            Optional<Role> role = roleRepository.findByName(roleName);
            Set<Role> roles = new HashSet<>();
            role.ifPresent(roles::add);
            user.setRoles(roles);
            
            // Save user
            User savedUser = userService.createUser(user);
            
            // Generate token
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
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
