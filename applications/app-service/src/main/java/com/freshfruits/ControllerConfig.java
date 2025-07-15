package com.freshfruits;

import com.freshfruits.domain.gateway.ProductRepository;
import com.freshfruits.usecase.ProductController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {

    @Bean
    public ProductController productController(ProductRepository productRepository) {
        return new ProductController(productRepository);
    }
}
