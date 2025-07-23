package com.freshfruits.postgresql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshfruits.domain.common.exception.ObjectMapperException;
import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ResponseSave;
import com.freshfruits.domain.gateway.ProductRepository;
import com.freshfruits.postgresql.data.ResponseSaveDto;
import com.freshfruits.postgresql.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

import static com.freshfruits.postgresql.enums.PostgresEnum.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements ProductRepository {

    @Value("${function.save.product}")
    private String functionSaveProduct;

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
                    .retryWhen(getRetrySpec());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private ResponseSaveDto buildResponseSave(io.r2dbc.spi.Readable rowMetadata) {
        return ResponseSaveDto.builder()
                .status(rowMetadata.get("status", Boolean.class))
                .message(rowMetadata.get("message", String.class))
                .build();
    }

    private Mono<ResponseSave> mapperSaveProduct(ResponseSaveDto responseSaveDto) {
        try {
            return Mono.just(ProductMapper.INSTANCE.toDomainSave(responseSaveDto));
        } catch (Exception exception) {
            throw new ObjectMapperException("Error al mapear la respuesta del save. Error: "
                    .concat(exception.getMessage()));
        }
    }

    private static RetryBackoffSpec getRetrySpec() {
        return Retry.fixedDelay(1, Duration.ofMillis(1))
                .doAfterRetry(retrySignal -> log.info(RETRY_COUNT.getMessage(),
                        retrySignal.totalRetriesInARow() + 1))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }
}
