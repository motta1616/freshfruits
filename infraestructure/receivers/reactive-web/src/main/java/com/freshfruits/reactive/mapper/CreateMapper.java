package com.freshfruits.reactive.mapper;

import com.freshfruits.domain.entities.CreateResponse;
import com.freshfruits.domain.entities.Product;
import com.freshfruits.reactive.dto.CreateResponseDto;
import com.freshfruits.reactive.dto.ProductoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateMapper {

    CreateMapper INSTANCE = Mappers.getMapper(CreateMapper.class);

    Product toDomain(ProductoDto productoDto);
    CreateResponseDto toDto(CreateResponse createResponse);
    ProductoDto toDto(Product product);
}
