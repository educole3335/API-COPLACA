package com.coplaca.apirest.mapper;

import com.coplaca.apirest.dto.AddressDTO;
import com.coplaca.apirest.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    AddressDTO toDTO(Address address);

    Address toEntity(AddressDTO dto);
}
