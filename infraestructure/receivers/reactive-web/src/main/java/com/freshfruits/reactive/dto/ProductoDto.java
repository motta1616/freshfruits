package com.freshfruits.reactive.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductoDto {

    private String id;
    private String nombre;
    private Double precio;
    private String status;
    private String message;
}
