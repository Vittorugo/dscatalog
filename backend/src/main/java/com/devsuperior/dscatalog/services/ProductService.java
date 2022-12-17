package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(PageRequest pageRequest) {
        Page<Product> response = repository.findAll(pageRequest);
        return response.map(product -> ProductDto.withCategories(product, product.getCategories()));
    }

    @Transactional
    public ProductDto findById(Long id) {
        Product product = repository.findById(id).orElseThrow( () -> new EntityNotFoundException("No entity found for the given id"));
        return ProductDto.withCategories(product, product.getCategories());
    }

    @Transactional
    public ProductDto create(ProductDto productDto) {
        Product newProduct = new Product();
        convertDtoToEntity(productDto, newProduct);
        return new ProductDto(repository.save(newProduct));
    }

    public ProductDto update(Long id, ProductDto dto) {
        try {
            Product product = repository.getReferenceById(id);
            convertDtoToEntity(dto, product);
            return new ProductDto(repository.save(product));
        } catch (javax.persistence.EntityNotFoundException exception) {
            throw new EntityNotFoundException(exception.getMessage());
        }
    }

    private void convertDtoToEntity(ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.getCategories().clear();

        for (CategoryDTO categoryDTO : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(categoryDTO.getId());
            entity.getCategories().add(category);
        }
    }
}
