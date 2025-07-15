package com.freshfruits.domain.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Product {

    private final String id;
    private final String nombre;
    private final Double precio;
}
