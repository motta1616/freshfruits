package com.freshfruits.domain.factories;

import com.freshfruits.domain.common.exception.BusinessException;
import com.freshfruits.domain.entities.ProductsRequest;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ValidateField {

    default Boolean validateFieldString(String string) {
        return Optional.ofNullable(string).isPresent();
    }

    default Mono<ProductsRequest> validatePageNumber(ProductsRequest productsRequest) {
        return Optional.ofNullable(productsRequest.getPageNumber()).isPresent()
                ? Mono.just(productsRequest)
                : Mono.error(new BusinessException(BusinessException.Type.VALIDATE_PAGE_NUMBER));
    }

    default Mono<ProductsRequest> validatePageSize(ProductsRequest productsRequest) {
        return Optional.ofNullable(productsRequest.getPageSize()).isPresent()
                ? Mono.just(productsRequest)
                : Mono.error(new BusinessException(BusinessException.Type.VALIDATE_PAGE_SIZE));
    }
}