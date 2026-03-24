package com.coplaca.apirest.mapper;

import com.coplaca.apirest.dto.SeasonalOfferDTO;
import com.coplaca.apirest.entity.SeasonalOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeasonalOfferMapper {

    @Mapping(source = "product.id", target = "productId")
    SeasonalOfferDTO toDTO(SeasonalOffer offer);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    SeasonalOffer toEntity(SeasonalOfferDTO dto);
}
