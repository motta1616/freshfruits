package com.freshfruits.postgresql.mapper;

import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ResponseSave;
import com.freshfruits.postgresql.data.ResponseSaveDto;
import com.freshfruits.reactive.dto.ProductoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ResponseSave toDomainSave(ResponseSaveDto responseSaveDto);
    Product toDomainFind(ProductoDto productDto);
    List<Product> toDomainAllFind(List<ProductoDto> listProductDto);
}
