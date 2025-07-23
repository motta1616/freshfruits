package com.freshfruits.domain.factories;

import com.freshfruits.domain.entities.CreateResponse;
import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ResponseSave;
import reactor.core.publisher.Mono;

import static com.freshfruits.domain.common.enums.Constants.*;

public interface BuildMessages {

    default Mono<CreateResponse> buildResponseSuccess(ResponseSave responseSave) {
        return Mono.just(CreateResponse.builder()
                .status(SUCCESS.getMessage())
                .message(responseSave.getMessage())
                .build());
    }

    default Mono<CreateResponse> buildResponseBrule(Product product, String message) {
        return Mono.just(CreateResponse.builder()
                .status(BAD_REQUEST.getMessage())
                .message(String.format(message, product.getId()))
                .build());
    }

    default Mono<CreateResponse> buildResponseTechnical(Product product, String message) {
        return Mono.just(CreateResponse.builder()
                .status(INTERNAL_SERVER_ERROR.getMessage())
                .message(String.format(message, product.getId()))
                .build());
    }
}