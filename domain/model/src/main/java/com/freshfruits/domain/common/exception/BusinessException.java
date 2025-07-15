package com.freshfruits.domain.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends ApplicationException {

    @Getter
    public enum Type {

        NULL_OR_EMPTY_FIELD("El valor del campo no puede ser nulo o vació. Campo:"),
        ERROR_CANCEL_AND_RETURN("Los campos IsCancel y IsReturn no pueden ser iguales o nulos");

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