package com.freshfruits.postgresql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshfruits.domain.common.exception.ObjectMapperException;
import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ResponseSave;
import com.freshfruits.domain.gateway.ProductRepository;
import com.freshfruits.postgresql.data.ResponseSaveDto;
import com.freshfruits.postgresql.mapper.BuildMessage;
import com.freshfruits.postgresql.mapper.ProductMapper;
import com.freshfruits.reactive.dto.ProductoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.function.Predicate;

import static com.freshfruits.postgresql.enums.PostgresEnum.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements ProductRepository, BuildMessage {

    @Value("${function.save.product}")
    private String functionSaveProduct;

    @Value("${function.find.product}")
    private String functionFindProduct;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<ResponseSave> saveProduct(Product product) {
        try {
            return r2dbcEntityTemplate.getDatabaseClient().sql(functionSaveProduct)
                    .bind("$1", objectMapper.writeValueAsString(product))
                    .map((row, rowMetadata) -> buildResponseSave(row))
                    .one()
                    .flatMap(this::mapperSaveProduct)
                    .retryWhen(getRetrySpec((throwable -> !(throwable instanceof IllegalArgumentException))));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<Product> findProduct(String id) {
        try {
            return r2dbcEntityTemplate.getDatabaseClient().sql(functionFindProduct)
                    .bind("id", id)
                    .map((row, rowMetadata) -> buildResponseFind(row))
                    .one()
                    .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontró el producto: ".concat(id))))
                    .flatMap(this::mapperFindProduct)
                    .retryWhen(getRetrySpec(throwable -> !(throwable instanceof IllegalArgumentException)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private Mono<ResponseSave> mapperSaveProduct(ResponseSaveDto responseSaveDto) {
        try {
            return Mono.just(ProductMapper.INSTANCE.toDomainSave(responseSaveDto));
        } catch (Exception exception) {
            throw new ObjectMapperException("Error al mapear la respuesta del save. Error: "
                    .concat(exception.getMessage()));
        }
    }

    private Mono<Product> mapperFindProduct(ProductoDto productoDto) {
        try {
            return Mono.just(ProductMapper.INSTANCE.toDomainFind(productoDto));
        } catch (Exception exception) {
            throw new ObjectMapperException("Error al mapear la respuesta del find. Error: "
                    .concat(exception.getMessage()));
        }
    }

    private static RetryBackoffSpec getRetrySpec(Predicate<Throwable> shouldRetry) {
        return Retry.fixedDelay(1, Duration.ofMillis(1))
                .filter(shouldRetry)
                .doAfterRetry(retrySignal -> log.info(RETRY_COUNT.getMessage(),
                        retrySignal.totalRetriesInARow() + 1))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }
}