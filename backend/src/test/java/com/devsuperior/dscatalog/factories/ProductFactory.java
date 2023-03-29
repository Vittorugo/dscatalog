package com.devsuperior.dscatalog.factories;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;
import java.util.HashSet;

public class ProductFactory {

    public static Product createProduct() {
        var categories = new HashSet<Category>();
        categories.add(new Category(1L, "TECH", Instant.now(), Instant.now()));

        return Product.builder()
                .id(1L)
                .name("Agua")
                .description("Agua indaia")
                .imgUrl("xxx")
                .updatedAt(Instant.now())
                .date(Instant.parse("2020-07-14T10:00:00Z"))
                .price(2.50)
                .categories(categories)
                .build();
    }

    public static ProductDto createProductDto() {
        Product product = createProduct();
        var categories = new HashSet<Category>();
        categories.add(new Category(2L, "GAME", Instant.now(), Instant.now()));
        return new ProductDto(product, categories);
    }
}
