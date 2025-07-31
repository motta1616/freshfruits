package com.freshfruits.reactive.mapper;

import com.freshfruits.domain.common.exception.BusinessException;
import com.freshfruits.reactive.dto.ProductoDto;
import reactor.core.publisher.Mono;

public interface BuildMessage {

    default Mono<ProductoDto> buildResponseError(Throwable throwable) {
        return Mono.just(ProductoDto.builder()
                .status((throwable instanceof BusinessException || throwable instanceof IllegalArgumentException)
                        ? "400"
                        : "500")
                .message(throwable.getMessage())
                .build());
    }
}