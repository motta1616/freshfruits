package com.freshfruits.usecase.helpers;

import com.freshfruits.domain.entities.Product;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.freshfruits.domain.common.enums.Constants.*;

@Slf4j
public class Traceability {
    protected Mono<Product> traceLogIn(Product product, String status, String message, String operation ) {
        String id = Optional.ofNullable(product.getId()).orElse("N/A");
        log.info(String.format(TRACE_MESSAGE.getMessage(), id, "IN", status,
                message, operation, product));
        return Mono.just(product);
    }

    protected Mono<Product> traceLogOut(Product product, String status, String message, String operation ) {
        log.info(String.format(TRACE_MESSAGE.getMessage(), product.getId(), "OUT", status, message, operation, product));
        return Mono.just(product);
    }
}
