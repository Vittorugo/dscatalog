package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 999L;
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShoulWhenThrowExceptionIdNotExists() {
        Mockito.doThrow(EntityNotFoundException.class).when(productRepository).deleteById(nonExistingId);

        Assertions.assertThrows( EntityNotFoundException.class,() -> {
            productService.delete(nonExistingId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
    }
}
