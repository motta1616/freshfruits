package com.freshfruits.postgresql;

import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ResponseSave;
import com.freshfruits.domain.gateway.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements ProductRepository {

    @Value("${function.save.product}")
    private String functionSaveProduct;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    @Override
    public Mono<ResponseSave> saveProduct(Product product) {
        try {
            return r2dbcEntityTemplate.getDatabaseClient().sql(functionSaveProduct)
                    .bind(ORDER_ID.getMessage(), orderId)
                    .map(consultAllParentMapper)
                    .one()
                    .retryWhen(Retry.fixedDelay(retry, Duration.ofSeconds(1))
                            .doAfterRetry(retrySignal -> log.info(RETRY_COUNT.getMessage(),
                                    retrySignal.totalRetriesInARow()))
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure()));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
