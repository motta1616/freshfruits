package com.freshfruits.domain.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends ApplicationException {

    @Getter
    public enum Type {

        INPUT_MESSAGE_BRULE("El mensaje no contiene un id."),
        OUTPUT_MESSAGE_FIND_BRULE("No se encontro registro para el id:");

        private final String message;

        public BusinessException build() {
            return new BusinessException(this, "");
        }

        Type(String message) {
            this.message = message;
        }
    }

    private final Type type;

    public BusinessException(Type type, String exceptionInfo) {
        super(type.message + " " + exceptionInfo);
        this.type = type;
    }

    public BusinessException(Type type) {
        super(type.message);
        this.type = type;
    }

    @Override
    public String getCode() {
        return type.name();
    }
}