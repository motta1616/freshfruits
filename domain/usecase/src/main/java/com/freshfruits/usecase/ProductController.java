package com.freshfruits.usecase;

import com.freshfruits.domain.common.exception.BusinessException;
import com.freshfruits.domain.entities.CreateResponse;
import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ResponseSave;
import com.freshfruits.domain.factories.BuildMessages;
import com.freshfruits.domain.factories.ValidateField;
import com.freshfruits.domain.gateway.ProductRepository;
import com.freshfruits.usecase.helpers.Traceability;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static com.freshfruits.domain.common.enums.Constants.*;

@RequiredArgsConstructor
public class ProductController extends Traceability implements BuildMessages, ValidateField {

    private final ProductRepository productRepository;

    public Mono<CreateResponse> createProduct(Product product) {
        return validateId(product.getId())
                ? traceIn(product, OK.getMessage(), INPUT_MESSAGE_CREATE_OK.getMessage(), CREATE_PRODUCT.getMessage())
                .flatMap(this::saveProductProcess)
                : traceIn(product, BRULE.getMessage(), INPUT_MESSAGE_BRULE.getMessage(), CREATE_PRODUCT.getMessage())
                .then(buildResponseBrule(product, INPUT_MESSAGE_BRULE.getMessage()));
    }

    private Mono<CreateResponse> saveProductProcess(Product product) {
        return productRepository.saveProduct(product)
                .flatMap(responseSave -> validateResponse(responseSave, product))
                .onErrorResume(throwable -> validateTraceError(product, throwable, SAVE_PRODUCT_PROCESS.getMessage())
                        .flatMap(productos1 -> validateResponseError(productos1, throwable)));
    }

    private Mono<CreateResponse> validateResponse(ResponseSave responseSave, Product product) {
        return Boolean.TRUE.equals(responseSave.getStatus())
                ? buildResponseSuccess(responseSave.getMessage())
                : buildResponseBrule(product, responseSave.getMessage());
    }

    private Mono<Product> validateTraceError(Product product, Throwable throwable, String operation) {
        return throwable instanceof BusinessException
                ? traceLogOut(product, BRULE.getMessage(), throwable.getMessage(), operation)
                : traceLogOut(product, ERROR.getMessage(), throwable.getMessage(), operation);
    }

    private Mono<CreateResponse> validateResponseError(Product product, Throwable throwable) {
        return throwable instanceof BusinessException
                ? buildResponseBrule(product, throwable.getMessage())
                : buildResponseTechnical(product, throwable.getMessage());
    }

    public Mono<Product> findProduct(Product product) {
        return validateId(product.getId())
                ? traceIn(product, OK.getMessage(), INPUT_MESSAGE_FIND_OK.getMessage(), FIND_PRODUCT.getMessage())
                .thenReturn(product)
                : traceIn(product, BRULE.getMessage(), INPUT_MESSAGE_BRULE.getMessage(), FIND_PRODUCT.getMessage())
                .then(Mono.error(new BusinessException(BusinessException.Type.INPUT_MESSAGE_BRULE)));
    }

    private Mono<Product> traceIn(Product product, String status, String message, String operation) {
        return traceLogIn(product, status, message, operation);
    }
}