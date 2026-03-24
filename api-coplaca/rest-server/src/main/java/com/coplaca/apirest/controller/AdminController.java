package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        userService.disableUser(id);
        return ResponseHelper.noContent();
    }

    @GetMapping(ApiConstants.STATS + "/top-products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<String>>> topProductsLastMonth() {
        LocalDateTime since = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        return ResponseHelper.ok(orderService.getTopProductsSince(since));
    }

    @GetMapping(ApiConstants.STATS + "/products-detailed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<String>>> detailedProductStats() {
        LocalDateTime since = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        return ResponseHelper.ok(orderService.getTopProductsSince(since));
    }

    @GetMapping(ApiConstants.STATS + "/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> orderStats(
            @RequestParam(required = false) String period) {
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalOrders", 0);
        stats.put("completedOrders", 0);
        stats.put("averageOrderValue", 0);
        stats.put("revenue", 0);
        return ResponseHelper.ok(stats);
    }

    @GetMapping(ApiConstants.STATS + "/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> userStats() {
        Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalUsers", userService.getAllUsers().size());
        stats.put("activeUsers", 0);
        stats.put("byRole", new java.util.HashMap<>());
        return ResponseHelper.ok(stats);
    }

    @GetMapping("/orders/today")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<List<Object>>> ordersToday() {
        return ResponseHelper.ok(new java.util.ArrayList<>());
    }
}
