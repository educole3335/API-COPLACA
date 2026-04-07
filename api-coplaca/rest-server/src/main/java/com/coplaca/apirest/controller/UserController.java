package com.coplaca.apirest.controller;

import com.coplaca.apirest.constants.ApiConstants;
import com.coplaca.apirest.dto.SuccessResponse;
import com.coplaca.apirest.dto.UpdateUserRequest;
import com.coplaca.apirest.dto.UserDTO;
import com.coplaca.apirest.entity.DeliveryAgentStatus;
import com.coplaca.apirest.service.UserService;
import com.coplaca.apirest.util.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.API_V1 + ApiConstants.USERS)
@Tag(name = "05 - Usuarios", description = "Perfil propio, gestión administrativa y estado de reparto")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(ApiConstants.CURRENT_USER)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener perfil propio", description = "Devuelve los datos del usuario autenticado")
    public ResponseEntity<SuccessResponse<UserDTO>> getCurrentUser(Authentication authentication) {
        return ResponseHelper.ok(userService.getCurrentUser(authentication.getName()));
    }

    @PutMapping(ApiConstants.CURRENT_USER)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar perfil propio", description = "Actualiza los datos básicos del usuario autenticado")
    public ResponseEntity<SuccessResponse<UserDTO>> updateCurrentUser(
            Authentication authentication,
            @RequestBody UpdateUserRequest userDetails) {
        return ResponseHelper.ok(
            userService.updateCurrentUser(authentication.getName(), userDetails),
            "Profile updated successfully"
        );
    }

    @DeleteMapping(ApiConstants.CURRENT_USER)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Desactivar cuenta propia", description = "Deshabilita la cuenta del usuario autenticado")
    public ResponseEntity<Void> deleteCurrentUser(Authentication authentication) {
        userService.disableCurrentUser(authentication.getName());
        return ResponseHelper.noContent();
    }

    @PatchMapping(ApiConstants.CURRENT_USER + "/delivery-status")
    @PreAuthorize("hasRole('DELIVERY')")
    @Operation(summary = "Actualizar estado de reparto", description = "Cambia el estado operativo del repartidor autenticado")
    public ResponseEntity<SuccessResponse<UserDTO>> updateDeliveryStatus(
            Authentication authentication,
            @RequestParam DeliveryAgentStatus status) {
        return ResponseHelper.ok(
            userService.updateDeliveryStatus(authentication.getName(), status),
            "Delivery status updated"
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener usuario por ID", description = "Consulta un usuario concreto para administración")
    public ResponseEntity<SuccessResponse<UserDTO>> getUser(@PathVariable Long id) {
        return ResponseHelper.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar usuario", description = "Actualización administrativa de un usuario existente")
    public ResponseEntity<SuccessResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest userDetails) {
        return ResponseHelper.ok(userService.updateUser(id, userDetails), "User updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar usuario", description = "Deshabilita una cuenta desde administración")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseHelper.noContent();
    }
}
