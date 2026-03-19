package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.SignUpRequest;
import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.service.OrderService;
import com.coplaca.apirest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/admin")
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
        UserDTO dto = userService.changeRoles(id, Set.copyOf(roleNames));
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/users/internal")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createInternalUser(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(userService.getUserById(userService.createManagedUser(request).getId()));
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

    /**
     * Obtiene estadísticas detalladas de los productos más vendidos
     */
    @GetMapping("/stats/products-detailed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> detailedProductStats() {
        LocalDateTime since = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        List<String> topProducts = orderService.getTopProductsSince(since);
        return ResponseEntity.ok(topProducts);
    }

    /**
     * Obtiene estadísticas de órdenes
     */
    @GetMapping("/stats/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> orderStats(@RequestParam(required = false) String period) {
        // period: WEEK, MONTH, YEAR, ALL
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalOrders", 0);
        stats.put("completedOrders", 0);
        stats.put("averageOrderValue", 0);
        stats.put("revenue", 0);
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtiene estadísticas de usuarios
     */
    @GetMapping("/stats/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> userStats() {
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalUsers", userService.getAllUsers().size());
        stats.put("activeUsers", 0);
        stats.put("byRole", new java.util.HashMap<>());
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtiene lista de usuarios activos
     */
    @GetMapping("/users/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getActiveUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .filter(u -> u.isEnabled())
                .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * Obtiene usuarios deshabilitados
     */
    @GetMapping("/users/disabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getDisabledUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .filter(u -> !u.isEnabled())
                .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * Reactiva un usuario deshabilitado
     */
    @PostMapping("/users/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> reactivateUser(@PathVariable Long id) {
        UserDTO dto = userService.reactivateUser(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtiene órdenes del día
     */
    @GetMapping("/orders/today")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object>> ordersToday() {
        return ResponseEntity.ok(new java.util.ArrayList<>());
    }
}
