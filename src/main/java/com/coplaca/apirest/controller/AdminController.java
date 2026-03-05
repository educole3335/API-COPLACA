package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.entity.Role;
import com.coplaca.apirest.service.OrderService;
import com.coplaca.apirest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    private final UserService userService;
    private final OrderService orderService;

    public AdminController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> listUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> changeRoles(@PathVariable Long id, @RequestBody List<String> roleNames) {
        List<Role> roles = roleNames.stream()
                .map(r -> {
                    Role role = new Role();
                    role.setName(r);
                    return role;
                })
                .collect(Collectors.toList());
        UserDTO dto = userService.changeRoles(id, Set.copyOf(roles));
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/top-products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> topProductsLastMonth() {
        LocalDateTime since = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        List<String> top = orderService.getTopProductsSince(since);
        return ResponseEntity.ok(top);
    }
}
