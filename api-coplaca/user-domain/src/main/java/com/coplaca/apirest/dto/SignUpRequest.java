package com.coplaca.apirest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "SignUpRequest", description = "Datos públicos para registrar un cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {
    @Schema(description = "Correo electrónico del cliente", example = "nuevo.cliente@example.com", format = "email")
    private String email;

    @Schema(description = "Contraseña de acceso", example = "Cliente123!")
    private String password;

    @Schema(description = "Nombre del cliente", example = "María")
    private String firstName;

    @Schema(description = "Apellidos del cliente", example = "López")
    private String lastName;

    @Schema(description = "Teléfono de contacto", example = "659123456")
    private String phoneNumber;

    @Schema(description = "Dirección de entrega del cliente")
    private AddressDTO address;

    @Schema(description = "Almacén preferente opcional", example = "1")
    private Long warehouseId;

    @Schema(description = "Rol solicitado en el alta pública", example = "ROLE_CUSTOMER", allowableValues = {"ROLE_CUSTOMER", "CUSTOMER"})
    private String role;
}
