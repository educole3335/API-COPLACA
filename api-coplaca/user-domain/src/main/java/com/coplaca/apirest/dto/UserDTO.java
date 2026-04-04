package com.coplaca.apirest.dto;

import com.coplaca.apirest.entity.DeliveryAgentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImage;
    private String profileInitial;
    private BigDecimal accountBalance;
    private AddressDTO address;
    private Long warehouseId;
    private String warehouseName;
    private java.util.Set<String> roles;
    private DeliveryAgentStatus deliveryStatus;
    private boolean enabled;
}
