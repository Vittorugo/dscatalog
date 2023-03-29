package com.devsuperior.dscatalog.integration;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.factories.ProductFactory;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long existId;
    private Long nonExistId;
    private ProductDto dto;

    @BeforeEach
    void setUp() {
        existId = 1L;
        nonExistId = -999L;
        dto = ProductFactory.createProductDto();
    }

    @Test
    public void findAllShouldReturnPage() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductDto> page = productService.findAll(pageRequest);

        Assertions.assertEquals(25, page.getTotalElements());
        Assertions.assertFalse(page.isLast());
    }

    @Test
    public void findAllShouldReturnSortedPage() {
        PageRequest pageRequest = PageRequest.of(0, 25, Sort.Direction.ASC, "name");
        Page<ProductDto> page = productService.findAll(pageRequest);

        Assertions.assertEquals("Macbook Pro", page.getContent().get(0).getName());
        Assertions.assertEquals("The Lord of the Rings", page.getContent().get(24).getName());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExist() {
        ProductDto dto = productService.findById(existId);

        Assertions.assertEquals("Smart TV", dto.getName());
    }

    @Test
    public void findByIdShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
           productService.findById(nonExistId);
        });
    }

    @Test
    public void deleteShouldDeleteProductWhenIdExist() {
        var result = productService.delete(existId);
        Assertions.assertEquals("Product deleted", result);
        Assertions.assertEquals(24, productRepository.count());
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
           productService.delete(nonExistId);
        });
    }

    @Test
    public void insertShouldSaveProduct() {
        ProductDto product = productService.insert(dto);
        Assertions.assertEquals("Agua", product.getName());
        Assertions.assertEquals(2.50, product.getPrice());
    }

    @Test
    public void updateShouldUpdateProductWhenIdExist() {
        ProductDto newDto = productService.update(dto, existId);
        Assertions.assertEquals("Agua", newDto.getName());
    }

    @Test
    public void updateShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
           productService.update(dto, nonExistId);
        });
    }
}
