package com.freshfruits.postgresql.mapper;

import com.freshfruits.domain.entities.ResponseSave;
import com.freshfruits.postgresql.data.ResponseSaveDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ResponseSave toDomainSave(ResponseSaveDto responseSaveDto);
}
