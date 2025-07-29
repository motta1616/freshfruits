package com.freshfruits.domain.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ProductsRequest {
    private final Integer pageNumber;
    private final Integer pageSize ;
}
