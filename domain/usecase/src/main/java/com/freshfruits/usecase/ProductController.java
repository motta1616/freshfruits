package com.freshfruits.usecase;

import com.freshfruits.domain.common.exception.BusinessException;
import com.freshfruits.domain.entities.CreateResponse;
import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.factories.BuildMessages;
import com.freshfruits.domain.gateway.ProductRepository;
import com.freshfruits.usecase.helpers.Traceability;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.freshfruits.domain.common.enums.Constants.*;

@RequiredArgsConstructor
public class ProductController extends Traceability implements BuildMessages {

    private final ProductRepository productRepository;

    public Mono<CreateResponse> createProduct(Product product) {
        return Optional.ofNullable(product.getId()).isPresent()
                ? traceIn(product)
                .flatMap(this::createProcess)
                : traceInError(product);
    }

    private Mono<CreateResponse> createProcess(Product product) {
        return saveProductProcess(product);
    }

    private Mono<CreateResponse> saveProductProcess(Product product) {
        return productRepository.saveProduct(product)
                .flatMap(products -> buildResponseBrule(product, INPUT_MESSAGE_BRULE.getMessage()))
                .onErrorResume(throwable -> validateTraceError(product, throwable, SAVE_PRODUCT_PROCESS.getMessage())
                        .flatMap(productos1 -> validateResponseError(productos1, throwable)));
    }

    private Mono<Product> traceIn(Product product) {
        return traceLogIn(product, OK.getMessage(), INPUT_MESSAGE_OK.getMessage(),
                CREATE_PRODUCT.getMessage());
    }

    private Mono<CreateResponse> traceInError(Product productos) {
        return traceLogIn(productos, BRULE.getMessage(), INPUT_MESSAGE_BRULE.getMessage(),
                CREATE_PRODUCT.getMessage())
                .flatMap(product -> buildResponseBrule(product, INPUT_MESSAGE_BRULE.getMessage()));
    }

    private Mono<CreateResponse> validateResponseError(Product product, Throwable throwable) {
        return throwable instanceof BusinessException
                ? buildResponseBrule(product, throwable.getMessage())
                : buildResponseTechnical(product, throwable.getMessage());
    }

    private Mono<Product> validateTraceError(Product product, Throwable throwable, String operation) {
        return throwable instanceof BusinessException
                ? traceLogIn(product, BRULE.getMessage(), throwable.getMessage(), operation)
                : traceLogIn(product, ERROR.getMessage(), throwable.getMessage(), operation);
    }
}