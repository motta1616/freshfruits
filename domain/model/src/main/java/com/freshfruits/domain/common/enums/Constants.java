package com.freshfruits.domain.common.enums;

import lombok.Getter;

@Getter
public enum Constants {

    OK("OK"),
    BRULE("BRULE"),
    ERROR("ERROR"),
    SUCCESS("200"),
    BAD_REQUEST("400"),
    INTERNAL_SERVER_ERROR("500"),
    TRACE_MESSAGE("[Process]: %s. [Status]: %s. [Message]: %s. [Operation]: %s. [Data]: %s"),
    INPUT_MESSAGE_CREATE_OK("El mensaje para crear se recibio de forma correcta"),
    OUTPUT_MESSAGE_CREATE_OK("El producto se inserto de forma forma correcta"),
    INPUT_MESSAGE_BRULE("El mensaje no contiene un id."),
    INPUT_MESSAGE_FIND_OK("El mensaje para consultar se recibio de forma correcta"),
    INPUT_MESSAGE_ALL_FIND_OK("El mensaje para consultar maxiva se recibio de forma correcta"),
    SAVE_PRODUCT_PROCESS("save-product-process"),
    FIND_PRODUCT_PROCESS("find-product-process"),
    CREATE_PRODUCT("create-product"),
    FIND_PRODUCT("find-product"),
    VALIDATE_PAGE("validate-page"),
    ALL_FIND_PRODUCT_PROCESS("all-fin.product-process");

    private final String message;

    Constants(String message) {
        this.message = message;
    }
}
