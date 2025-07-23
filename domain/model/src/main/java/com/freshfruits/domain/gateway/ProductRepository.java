package com.freshfruits.domain.gateway;

import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ResponseSave;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<ResponseSave> saveProduct(Product product);
}
