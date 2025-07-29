package com.freshfruits.reactive;

import com.freshfruits.reactive.dto.CreateResponseDto;
import com.freshfruits.reactive.dto.ProductoDto;
import com.freshfruits.reactive.dto.ProductsRequestDto;
import com.freshfruits.reactive.mapper.BuildMessage;
import com.freshfruits.reactive.mapper.CreateMapper;
import com.freshfruits.usecase.ProductController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producto")
public class ProductService implements BuildMessage {

    private final ProductController productController;

    @PostMapping(path = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CreateResponseDto>> createProcess(@RequestBody ProductoDto productoDto) {
        return productController.createProduct(CreateMapper.INSTANCE.toDomain(productoDto))
                .map(createResponse -> ResponseEntity.status(Integer.parseInt(createResponse.getStatus()))
                        .body(CreateMapper.INSTANCE.toDto(createResponse)));
    }

    @GetMapping(path = "/find", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ProductoDto>> findProcess(@RequestBody ProductoDto productoDto) {
        return productController.findProduct(CreateMapper.INSTANCE.toDomain(productoDto))
                .map(product -> ResponseEntity.ok(CreateMapper.INSTANCE.toDto(product)))
                .onErrorResume(throwable -> buildResponseError(throwable)
                        .map(productDtoUpdate -> ResponseEntity
                                .status(Integer.parseInt(productDtoUpdate.getStatus()))
                                .body(productDtoUpdate)));
    }

    @GetMapping(path = "/allFind", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ProductoDto>>> allFindProcess(@RequestBody ProductsRequestDto productsRequestDto) {
        return productController.allFindProduct(CreateMapper.INSTANCE.toDomain(productsRequestDto))
                .map(products -> ResponseEntity.ok(CreateMapper.INSTANCE.toDto(products)))
                .onErrorResume(throwable -> buildResponseError(throwable)
                        .map(productDtoUpdate -> ResponseEntity
                                .status(Integer.parseInt(productDtoUpdate.getStatus()))
                                .body(List.of(productDtoUpdate))));
    }
}