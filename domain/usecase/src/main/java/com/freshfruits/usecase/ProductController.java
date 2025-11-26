package com.freshfruits.usecase;

import com.freshfruits.domain.common.exception.BusinessException;
import com.freshfruits.domain.entities.CreateResponse;
import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ProductsRequest;
import com.freshfruits.domain.entities.ResponseSave;
import com.freshfruits.domain.factories.BuildMessages;
import com.freshfruits.domain.factories.ValidateField;
import com.freshfruits.domain.gateway.ProductRepository;
import com.freshfruits.usecase.helpers.Traceability;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.freshfruits.domain.common.enums.Constants.*;

@RequiredArgsConstructor
public class ProductController extends Traceability implements BuildMessages, ValidateField {

    private final ProductRepository productRepository;

    public Mono<CreateResponse> createProduct(Product product) {
        return validateFieldString(product.getId())
                ? traceLogProductIn(product, OK.getMessage(), INPUT_MESSAGE_CREATE_OK.getMessage(),
                CREATE_PRODUCT.getMessage())
                .then(saveProductProcess(product))
                : traceLogProductIn(product, BRULE.getMessage(), INPUT_MESSAGE_BRULE.getMessage(),
                CREATE_PRODUCT.getMessage())
                .then(buildResponseBrule(product, INPUT_MESSAGE_BRULE.getMessage()));
    }

    private Mono<CreateResponse> saveProductProcess(Product product) {
        return productRepository.saveProduct(product)
                .flatMap(responseSave -> validateResponseSave(responseSave, product))
                .onErrorResume(throwable -> validateTraceError(product, throwable,
                        SAVE_PRODUCT_PROCESS.getMessage())
                        .flatMap(productSave -> validateResponseError(productSave, throwable)));
    }

    private Mono<CreateResponse> validateResponseSave(ResponseSave responseSave, Product product) {
        return Boolean.TRUE.equals(responseSave.getStatus())
                ? traceLogOut(product, OK.getMessage(), responseSave.getMessage(), SAVE_PRODUCT_PROCESS.getMessage())
                .then(buildResponseSuccess(responseSave.getMessage()))
                : traceLogOut(product, BRULE.getMessage(), responseSave.getMessage(), SAVE_PRODUCT_PROCESS.getMessage())
                .then(buildResponseBrule(product, responseSave.getMessage()));
    }

    private Mono<CreateResponse> validateResponseError(Product product, Throwable throwable) {
        return throwable instanceof IllegalArgumentException
                ? buildResponseBrule(product, throwable.getMessage())
                : buildResponseTechnical(product, throwable.getMessage());
    }

    public Mono<Product> findProduct(Product product) {
        return validateFieldString(product.getId())
                ? traceLogProductIn(product, OK.getMessage(), INPUT_MESSAGE_FIND_OK.getMessage(), FIND_PRODUCT.getMessage())
                .then(findProductProcess(product))
                : traceLogProductIn(product, BRULE.getMessage(), INPUT_MESSAGE_BRULE.getMessage(), FIND_PRODUCT.getMessage())
                .then(Mono.error(new BusinessException(BusinessException.Type.INPUT_MESSAGE_BRULE)));
    }

    private Mono<Product> findProductProcess(Product product) {
        return productRepository.findProduct(product.getId())
                .onErrorResume(throwable -> validateTraceError(product, throwable,
                        FIND_PRODUCT_PROCESS.getMessage())
                        .then(Mono.error(throwable)));
    }

    private Mono<Product> validateTraceError(Product product, Throwable throwable, String operation) {
        return throwable instanceof IllegalArgumentException
                ? traceLogOut(product, BRULE.getMessage(), throwable.getMessage(), operation)
                .thenReturn(product)
                : traceLogOut(product, ERROR.getMessage(), throwable.getMessage(), operation)
                .thenReturn(product);
    }

    public Mono<List<Product>> allFindProduct(ProductsRequest productsRequest) {
        return validatePage(productsRequest)
                .flatMap(this::allFindProductProcess);
    }

    private Mono<List<Product>> allFindProductProcess(ProductsRequest productsRequest) {
        return productRepository.allFindProduct(productsRequest)
                .onErrorResume(throwable -> validateTraceAllError(productsRequest, throwable,
                        ALL_FIND_PRODUCT_PROCESS.getMessage())
                        .then(Mono.error(throwable)));
    }

    private Mono<ProductsRequest> validatePage(ProductsRequest productsRequest) {
        return validatePageNumber(productsRequest)
                .flatMap(this::validatePageSize)
                .flatMap(productsRequests -> traceLogProductIn(productsRequests, OK.getMessage(),
                        INPUT_MESSAGE_ALL_FIND_OK.getMessage(), VALIDATE_PAGE.getMessage())
                        .thenReturn(productsRequests))
                .onErrorResume(throwable -> traceLogProductIn(productsRequest,
                        BRULE.getMessage(), throwable.getMessage(), VALIDATE_PAGE.getMessage())
                        .then(Mono.error(throwable)));
    }

    private Mono<ProductsRequest> validateTraceAllError(ProductsRequest productsRequest,
                                                        Throwable throwable,
                                                        String operation) {

        return throwable instanceof IllegalArgumentException
                ? traceLogOut(productsRequest, BRULE.getMessage(), throwable.getMessage(), operation)
                .thenReturn(productsRequest)
                : traceLogOut(productsRequest, ERROR.getMessage(), throwable.getMessage(), operation)
                .thenReturn(productsRequest);
    }
}