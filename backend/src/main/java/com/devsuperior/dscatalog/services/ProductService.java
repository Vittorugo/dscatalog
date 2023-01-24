package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Page<ProductDto> findAll(PageRequest pageRequest) {
        Page<Product> response = repository.findAll(pageRequest);
        return response.map(ProductDto::new);
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        Optional<Product> product = repository.findById(id);
        Product entity = product.orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return new ProductDto(entity, product.get().getCategories());
    }

    @Transactional
    public ProductDto update(ProductDto productDto, Long id) {
        try {
            Product product = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
            copyDtoToProduct(productDto, product);
            return new ProductDto(repository.save(product));
        } catch (javax.persistence.EntityNotFoundException e) {
            throw new EntityNotFoundException("Fail entity updated");
        }
    }

    @Transactional
    public ProductDto insert(ProductDto dto) {
        Product product = new Product();
        copyDtoToProduct(dto, product);
        return new ProductDto(repository.save(product));
    }

    public String delete(Long id) {
        try {
            repository.deleteById(id);
            return "Product delete successful";
        } catch (EmptyResultDataAccessException exception) {
            throw new EntityNotFoundException("Product ID Not Found");
        } catch (DataIntegrityViolationException exception) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDtoToProduct(ProductDto productDto, Product entity) {
        entity.setName(productDto.getName());
        entity.setDate(productDto.getDate());
        entity.setPrice(productDto.getPrice());
        entity.setDescription(productDto.getDescription());
        entity.setImgUrl(productDto.getImgUrl());

        entity.getCategories().clear();
        productDto.getCategories().forEach(
                dto -> {
                    Category category = categoryRepository.findById(dto.getId()).get();
                    entity.getCategories().add(category);
                }
        );
    }

}
