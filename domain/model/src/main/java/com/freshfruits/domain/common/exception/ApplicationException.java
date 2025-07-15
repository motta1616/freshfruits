package com.freshfruits.domain.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final String code;

    public ApplicationException(String message) {
        this(message, null);
    }

    public ApplicationException(String message, String code) {
        super(message);
        this.code = code;
    }

}
