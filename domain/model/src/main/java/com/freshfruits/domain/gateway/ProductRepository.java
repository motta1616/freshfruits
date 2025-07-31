package com.freshfruits.domain.gateway;

import com.freshfruits.domain.entities.Product;
import com.freshfruits.domain.entities.ProductsRequest;
import com.freshfruits.domain.entities.ResponseSave;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductRepository {

    Mono<ResponseSave> saveProduct(Product product);
    Mono<Product> findProduct(String id);
    Mono<List<Product>> allFindProduct(ProductsRequest productsRequest);
}
