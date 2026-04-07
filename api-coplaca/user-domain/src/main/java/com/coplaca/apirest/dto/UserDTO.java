package com.coplaca.apirest.dto;

import com.coplaca.apirest.entity.DeliveryAgentStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(name = "UserDTO", description = "Representación pública de un usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    @Schema(description = "ID del usuario", example = "5")
    private Long id;
    @Schema(description = "Correo electrónico", example = "cliente@example.com", format = "email")
    private String email;
    @Schema(description = "Nombre", example = "Juan")
    private String firstName;
    @Schema(description = "Apellidos", example = "García")
    private String lastName;
    @Schema(description = "Teléfono", example = "659123456")
    private String phoneNumber;
    @Schema(description = "URL de imagen de perfil", example = "https://example.com/avatar.png")
    private String profileImage;
    @Schema(description = "Inicial para avatar", example = "J")
    private String profileInitial;
    @Schema(description = "Saldo de cuenta", example = "0.00")
    private BigDecimal accountBalance;
    @Schema(description = "Dirección principal del usuario")
    private AddressDTO address;
    @Schema(description = "ID del almacén asignado", example = "1")
    private Long warehouseId;
    @Schema(description = "Nombre del almacén asignado", example = "Almacen Tenerife")
    private String warehouseName;
    @ArraySchema(schema = @Schema(description = "Roles de acceso", example = "CUSTOMER"))
    private java.util.Set<String> roles;
    @Schema(description = "Estado operativo del repartidor")
    private DeliveryAgentStatus deliveryStatus;
    @Schema(description = "Indica si la cuenta está activa", example = "true")
    private boolean enabled;
}
