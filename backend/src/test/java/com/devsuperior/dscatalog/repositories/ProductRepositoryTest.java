package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factories.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Long exitingId;
    private Product newProductTest;

    @BeforeEach
    void setUp() throws Exception {
        exitingId = 1L;
        newProductTest = ProductFactory.createProduct();
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(exitingId);
        Optional<Product> product = productRepository.findById(exitingId);
        Assertions.assertFalse(product.isPresent());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdNotExists() {
        Assertions.assertThrows( EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(80L);
        });
    }

    @Test
    public void saveShouldSaveObjectWhenIdIsNull() {
        newProductTest.setId(null);
        productRepository.save(newProductTest);

        Assertions.assertFalse(newProductTest.getId() == null, "Id não está Nulo");
        Assertions.assertNotNull(newProductTest.getId());
    }

    @Test
    public void findByIdShouldReturnNotEmptyIdWhenIdExist() {
        Optional product = productRepository.findById(exitingId);
        Assertions.assertTrue(product.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyWhenIdDoesNotExist() {
        Optional product = productRepository.findById(999L);
        Assertions.assertTrue(product.isEmpty());
        Assertions.assertFalse(product.isPresent());
    }
}
