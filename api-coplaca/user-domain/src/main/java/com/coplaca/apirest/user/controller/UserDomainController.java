package com.coplaca.apirest.user.controller;

import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.dto.UpdateUserRequest;
import com.coplaca.apirest.entity.DeliveryAgentStatus;
import com.coplaca.apirest.service.UserService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-domain")
@RequiredArgsConstructor
public class UserDomainController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}/roles")
    public ResponseEntity<UserDTO> changeRoles(@PathVariable Long id, @RequestParam Set<String> roles) {
        return ResponseEntity.ok(userService.changeRoles(id, roles));
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestParam String email) {
        return ResponseEntity.ok(userService.getCurrentUser(email));
    }

    @PutMapping("/users/me")
    public ResponseEntity<UserDTO> updateCurrentUser(@RequestParam String email, @RequestBody UpdateUserRequest userDetails) {
        return ResponseEntity.ok(userService.updateCurrentUser(email, userDetails));
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<Void> disableCurrentUser(@RequestParam String email) {
        userService.disableCurrentUser(email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/me/delivery-status")
    public ResponseEntity<UserDTO> updateDeliveryStatus(@RequestParam String email,
                                                        @RequestParam DeliveryAgentStatus status) {
        return ResponseEntity.ok(userService.updateDeliveryStatus(email, status));
    }

    @GetMapping("/users/warehouse/{warehouseId}/delivery-agents")
    public ResponseEntity<List<UserDTO>> getAvailableDeliveryAgents(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(userService.getAvailableDeliveryAgents(warehouseId));
    }
}
