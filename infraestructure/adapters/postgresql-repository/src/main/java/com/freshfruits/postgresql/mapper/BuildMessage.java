package com.freshfruits.postgresql.mapper;

import com.freshfruits.postgresql.data.ResponseSaveDto;

public interface BuildMessage {

    default ResponseSaveDto buildResponseSave(io.r2dbc.spi.Readable rowMetadata) {
        return ResponseSaveDto.builder()
                .status(rowMetadata.get("status", Boolean.class))
                .message(rowMetadata.get("message", String.class))
                .build();
    }
}