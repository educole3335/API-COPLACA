package com.coplaca.apirest.mapper;

import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.entity.Order;
import com.coplaca.apirest.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "deliveryAgent.id", target = "deliveryAgentId")
    @Mapping(source = "deliveryAgent", target = "deliveryAgentName", qualifiedByName = "userToFullName")
    @Mapping(source = "deliveryAddress.id", target = "deliveryAddressId")
    OrderDTO toDTO(Order order);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "deliveryAgent", ignore = true)
    @Mapping(target = "deliveryAddress", ignore = true)
    @Mapping(target = "stripePaymentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderDTO dto);

    @Named("userToFullName")
    default String userToFullName(User user) {
        if (user == null) {
            return null;
        }
        return user.getFirstName() + " " + user.getLastName();
    }
}
