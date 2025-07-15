package com.freshfruits.reactive;

import com.freshfruits.reactive.dto.CreateResponseDto;
import com.freshfruits.reactive.dto.ProductoDto;
import com.freshfruits.reactive.mapper.CreateMapper;
import com.freshfruits.usecase.ProductController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producto")
public class ProductService {

    private final ProductController productController;
    @PostMapping(path = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CreateResponseDto>> createProcess(
            @RequestBody ProductoDto productoDto) {
        return productController.createProduct(CreateMapper.INSTANCE.toDomain(productoDto))
                .map(createResponse -> ResponseEntity.status(Integer.parseInt(createResponse.getStatus()))
                        .body(CreateMapper.INSTANCE.toDto(createResponse)));
    }
}