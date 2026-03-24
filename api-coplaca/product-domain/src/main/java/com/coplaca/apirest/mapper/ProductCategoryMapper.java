package com.coplaca.apirest.mapper;

import com.coplaca.apirest.dto.ProductCategoryDTO;
import com.coplaca.apirest.entity.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductCategoryMapper {

    @Mapping(target = "imageUrl", source = "icon")
    @Mapping(target = "productCount", ignore = true)
    ProductCategoryDTO toDTO(ProductCategory category);

    @Mapping(target = "icon", source = "imageUrl")
    @Mapping(target = "color", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ProductCategory toEntity(ProductCategoryDTO dto);
}
