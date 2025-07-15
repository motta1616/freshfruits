package com.freshfruits.reactive.mapper;

import com.freshfruits.domain.entities.Product;
import com.freshfruits.reactive.dto.ProductoDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateMapperTest {

    @Test
    void toDomain() {
        // Arrange: Crear un ProductoDto con valores de prueba
        ProductoDto productoDto = new ProductoDto();
        productoDto.setNombre("Manzana");
        productoDto.setPrecio(1.5);

        // Act: Mapear ProductoDto a Productos usando CreateMapper
        Product product = CreateMapper.INSTANCE.toDomain(productoDto);

        // Assert: Verificar que los valores se hayan mapeado correctamente
        assertEquals("Manzana", product.getNombre());
        assertEquals(1.5, product.getPrecio());
    }
}