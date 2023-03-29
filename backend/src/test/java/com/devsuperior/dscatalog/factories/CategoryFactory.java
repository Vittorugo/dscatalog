package com.devsuperior.dscatalog.factories;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;

import java.time.Instant;

public class CategoryFactory {

    public static Category createCategory() {
        return Category.builder()
                .id(1L)
                .name("TECH")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public static CategoryDTO categoryCategoryDTO() {
        return new CategoryDTO(createCategory());
    }
}
