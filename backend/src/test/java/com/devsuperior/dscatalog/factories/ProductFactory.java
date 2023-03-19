package com.devsuperior.dscatalog.factories;

import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class ProductFactory {

    public static Product createProduct() {
        return Product.builder()
                .id(1L)
                .name("Agua")
                .description("Agua indaia")
                .imgUrl("xxx")
                .updatedAt(Instant.now())
                .date(Instant.parse("2020-07-14T10:00:00Z"))
                .price(2.50)
                .build();
    }
}
