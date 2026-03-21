package com.coplaca.apirest.controller;

import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.entity.DeliveryAgentStatus;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getCurrentUser(authentication.getName()));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> updateCurrentUser(Authentication authentication,
                                                     @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateCurrentUser(authentication.getName(), userDetails));
    }

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteCurrentUser(Authentication authentication) {
        userService.disableCurrentUser(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/delivery-status")
    @PreAuthorize("hasRole('DELIVERY')")
    public ResponseEntity<UserDTO> updateDeliveryStatus(Authentication authentication,
                                                        @RequestParam DeliveryAgentStatus status) {
        return ResponseEntity.ok(userService.updateDeliveryStatus(authentication.getName(), status));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody User userDetails) {
        UserDTO updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.noContent().build();
    }
}
