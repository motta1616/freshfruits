package com.freshfruits.postgresql.mapper;

import com.freshfruits.postgresql.data.ResponseSaveDto;
import com.freshfruits.reactive.dto.ProductoDto;

public interface BuildMessage {

    default ResponseSaveDto buildResponseSave(io.r2dbc.spi.Readable rowMetadata) {
        return ResponseSaveDto.builder()
                .status(rowMetadata.get("status", Boolean.class))
                .message(rowMetadata.get("message", String.class))
                .build();
    }

    default ProductoDto buildResponseFind(io.r2dbc.spi.Readable rowMetadata) {
        return ProductoDto.builder()
                .id(rowMetadata.get("id", String.class))
                .nombre(rowMetadata.get("nombre", String.class))
                .precio(rowMetadata.get("precio", Double.class))
                .build();
    }
}