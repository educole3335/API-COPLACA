package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "LoginRequest", description = "Credenciales para iniciar sesión")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @Schema(description = "Correo electrónico del usuario", example = "cliente@example.com", format = "email")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "Cliente123!")
    private String password;
}
