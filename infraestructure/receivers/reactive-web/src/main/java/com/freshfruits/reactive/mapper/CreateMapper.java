package com.freshfruits.reactive.mapper;

import com.freshfruits.domain.entities.CreateResponse;
import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ProductsRequest;
import com.freshfruits.reactive.dto.CreateResponseDto;
import com.freshfruits.reactive.dto.ProductoDto;
import com.freshfruits.reactive.dto.ProductsRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CreateMapper {

    CreateMapper INSTANCE = Mappers.getMapper(CreateMapper.class);

    Product toDomain(ProductoDto productoDto);
    ProductsRequest toDomain(ProductsRequestDto productsRequestDto);
    CreateResponseDto toDto(CreateResponse createResponse);
    ProductoDto toDto(Product product);
    List<ProductoDto> toDto(List<Product> products);
}
