package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.dto.SignUpRequest;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.service.OrderService;
import com.coplaca.apirest.service.UserService;
import com.coplaca.apirest.util.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.ADMIN)
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<UserDTO>>> listUsers() {
        return ResponseHelper.ok(userService.getAllUsers());
    }

    @GetMapping("/users" + ApiConstants.ACTIVE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<UserDTO>>> getActiveUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .filter(UserDTO::isEnabled)
                .toList();
        return ResponseHelper.ok(users);
    }

    @GetMapping("/users" + ApiConstants.DISABLED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<UserDTO>>> getDisabledUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .filter(u -> !u.isEnabled())
                .toList();
        return ResponseHelper.ok(users);
    }

    @PutMapping("/users/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<UserDTO>> changeRoles(
            @PathVariable Long id,
            @RequestBody List<String> roleNames) {
        return ResponseHelper.ok(userService.changeRoles(id, Set.copyOf(roleNames)), "User roles updated");
    }

    @PostMapping("/users/internal")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<UserDTO>> createInternalUser(@RequestBody SignUpRequest request) {
        UserDTO user = userService.getUserById(userService.createManagedUser(request).getId());
        return ResponseHelper.created(user, "Internal user created successfully");
    }

    @PostMapping("/users/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<UserDTO>> reactivateUser(@PathVariable Long id) {
        return ResponseHelper.ok(userService.reactivateUser(id), "User reactivated successfully");
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        orderService.validateUserCanBeDisabled(id);
        userService.disableUser(id);
        return ResponseHelper.noContent();
    }

    @GetMapping(ApiConstants.STATS + "/top-products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<Map<String, Object>>>> topProductsLastMonth() {
        LocalDateTime since = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        Map<String, Object> stats = orderService.getTopProductsDetailedSince(since);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> topProducts = (List<Map<String, Object>>) stats.get("topProducts");
        return ResponseHelper.ok(topProducts);
    }

    @GetMapping(ApiConstants.STATS + "/products-detailed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<String>>> detailedProductStats() {
        LocalDateTime since = LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
        return ResponseHelper.ok(orderService.getTopProductsSince(since));
    }

    @GetMapping(ApiConstants.STATS + "/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> orderStats(
            @RequestParam(required = false) String period) {
        return ResponseHelper.ok(orderService.getOrderStatsSince(resolveStatsStart(period)));
    }

    @GetMapping(ApiConstants.STATS + "/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> userStats() {
        List<UserDTO> users = userService.getAllUsers();

        Map<String, Long> byRole = users.stream()
                .flatMap(user -> user.getRoles().stream())
                .collect(Collectors.groupingBy(role -> role.toUpperCase(Locale.ROOT), Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", users.size());
        stats.put("activeUsers", users.stream().filter(UserDTO::isEnabled).count());
        stats.put("byRole", byRole);
        return ResponseHelper.ok(stats);
    }

    @GetMapping("/orders/today")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<OrderDTO>>> ordersToday() {
        return ResponseHelper.ok(orderService.getOrdersToday());
    }

    @GetMapping("/health")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            long userCount = userService.getAllUsers().size();
            return ResponseEntity.ok(Map.of(
                    "status", "UP",
                    "database", "CONNECTED",
                    "message", "Backend COPLACA conectado correctamente a la base de datos",
                    "usersInDatabase", userCount,
                    "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "DOWN",
                    "database", "ERROR",
                    "message", "No se puede conectar a la base de datos: " + e.getMessage(),
                    "timestamp", System.currentTimeMillis()
            ));
        }
    }

    private LocalDateTime resolveStatsStart(String period) {
        String normalized = period == null ? "month" : period.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "day", "today" -> LocalDateTime.now().minus(1, ChronoUnit.DAYS);
            case "week", "7d" -> LocalDateTime.now().minus(7, ChronoUnit.DAYS);
            case "month", "30d", "" -> LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
            default -> LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        };
    }
}
