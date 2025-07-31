package com.freshfruits.usecase.helpers;

import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ProductsRequest;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.freshfruits.domain.common.enums.Constants.*;

@Slf4j
public class Traceability {
    protected Mono<Product> traceLogProductIn(Product product,
                                              String status,
                                              String message,
                                              String operation ) {

        String id = Optional.ofNullable(product.getId()).orElse("N/A");
        log.info(String.format(TRACE_MESSAGE.getMessage(), id, "IN", status,
                message, operation, product));
        return Mono.just(product);
    }

    protected Mono<Product> traceLogOut(Product product,
                                        String status,
                                        String message,
                                        String operation ) {

        log.info(String.format(TRACE_MESSAGE.getMessage(), product.getId(), "OUT", status, message, operation, product));
        return Mono.just(product);
    }

    protected Mono<ProductsRequest> traceLogPageIn(ProductsRequest productsRequest,
                                                   String status,
                                                   String message,
                                                   String operation ) {

        log.info(String.format(TRACE_MESSAGE.getMessage(), "N/A", "IN", status,
                message, operation, productsRequest));
        return Mono.just(productsRequest);
    }

    protected Mono<ProductsRequest> traceLogPageOut(ProductsRequest productsRequest,
                                        String status,
                                        String message,
                                        String operation ) {

        log.info(String.format(TRACE_MESSAGE.getMessage(), "N/A", "OUT", status, message, operation, productsRequest));
        return Mono.just(productsRequest);
    }
}