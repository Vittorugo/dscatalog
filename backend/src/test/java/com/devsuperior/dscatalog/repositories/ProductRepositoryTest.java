package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
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

    @BeforeEach
    void setUp() throws Exception {
        exitingId = 1L;
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


}
