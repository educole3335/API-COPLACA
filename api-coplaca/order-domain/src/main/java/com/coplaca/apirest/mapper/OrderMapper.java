package com.coplaca.apirest.mapper;

import com.coplaca.apirest.dto.OrderDTO;
import com.coplaca.apirest.entity.Address;
import com.coplaca.apirest.entity.Order;
import com.coplaca.apirest.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "deliveryAgent.id", target = "deliveryAgentId")
    @Mapping(source = "deliveryAgent", target = "deliveryAgentName", qualifiedByName = "userToFullName")
    @Mapping(source = "deliveryAddress.id", target = "deliveryAddressId")
    @Mapping(source = "deliveryAddress", target = "deliveryAddressLabel", qualifiedByName = "addressToFullAddress")
    OrderDTO toDTO(Order order);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "deliveryAgent", ignore = true)
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

    @Named("addressToFullAddress")
    default String addressToFullAddress(Address address) {
        if (address == null) {
            return null;
        }

        List<String> parts = new ArrayList<>();
        String streetLine = buildStreetLine(address);
        appendIfPresent(parts, streetLine);
        appendIfPresent(parts, address.getPostalCode());
        appendIfPresent(parts, address.getCity());
        appendIfPresent(parts, address.getProvince());
        appendIfPresent(parts, address.getAdditionalInfo());

        return String.join(", ", parts);
    }

    default String buildStreetLine(Address address) {
        String street = normalizedValue(address.getStreet());
        if (street == null) {
            return null;
        }

        StringBuilder streetLine = new StringBuilder(street);
        String streetNumber = normalizedValue(address.getStreetNumber());
        if (streetNumber != null) {
            streetLine.append(' ').append(streetNumber);
        }

        String apartment = normalizedValue(address.getApartment());
        if (apartment != null) {
            streetLine.append(", ").append(apartment);
        }

        return streetLine.toString();
    }

    default void appendIfPresent(List<String> parts, String value) {
        String normalized = normalizedValue(value);
        if (normalized != null) {
            parts.add(normalized);
        }
    }

    default String normalizedValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
