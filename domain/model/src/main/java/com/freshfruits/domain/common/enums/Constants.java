package com.freshfruits.domain.common.enums;

import lombok.Getter;

@Getter
public enum Constants {

    OK("OK"),
    BRULE("BRULE"),
    ERROR("ERROR"),
    TRACE_MESSAGE("[Id]: %s. [Process]: %s. [Status]: %s. [Message]: %s. [Operation]: %s. [Data]: %s"),
    INPUT_MESSAGE_OK("El mensaje se recibe de forma correcta"),
    INPUT_MESSAGE_BRULE("El mensaje no contiene un id."),
    CREATE_PRODUCT("create-product"),
    SUCCESS("200"),
    BAD_REQUEST("400"),
    INTERNAL_SERVER_ERROR("500"),
    SAVE_PRODUCT_PROCESS("save-product-process"),
    ;

    private final String message;

    Constants(String message) {
        this.message = message;
    }
}
