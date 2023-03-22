package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factories.ProductFactory;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private Product product;
    private ProductDto dto;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 999L;
        product = ProductFactory.createProduct();
        dto = ProductFactory.createProductDto();
        page = new PageImpl<>(List.of(ProductFactory.createProduct(), ProductFactory.createProduct()));
    }

    @Test
    public void findAllShouldReturnAll() {
        when(productRepository.findAll((Pageable) any())).thenReturn(page);
        Assertions.assertFalse(productService.findAll(any()).isEmpty());

        verify(productRepository).findAll((Pageable) any());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExist() {
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        ProductDto result = productService.findById(existingId);

        Assertions.assertDoesNotThrow(() -> {
            productService.findById(existingId);
        });
        Assertions.assertNotNull(result);
        verify(productRepository, times(2)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowExceptionWhenIdNotExist() {
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Assertions.assertThrows( EntityNotFoundException.class, () -> {
            productService.findById(nonExistingId);
        });

        verify(productRepository).findById(nonExistingId);
    }

    @Test
    public void insertShouldInsertProduct() {
        when(productRepository.save(any())).thenReturn(product);
        Assertions.assertDoesNotThrow(() -> {
            productService.insert(ProductFactory.createProductDto());
        });

        //verify(productRepository).save(product);
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
