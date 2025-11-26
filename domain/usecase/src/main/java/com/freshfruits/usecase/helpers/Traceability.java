package com.freshfruits.usecase.helpers;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.freshfruits.domain.common.enums.Constants.*;

@Slf4j
public class Traceability {
    protected <T> Mono<Void> traceLogProductIn(T data,
                                              String status,
                                              String message,
                                              String operation ) {

        log.info(String.format(TRACE_MESSAGE.getMessage(), "IN", status,
                message, operation, data));
        return Mono.empty();
    }

    protected <T> Mono<Void> traceLogOut(T data,
                                        String status,
                                        String message,
                                        String operation ) {

        log.info(String.format(TRACE_MESSAGE.getMessage(), "OUT", status, message, operation, data));
        return Mono.empty();
    }
}