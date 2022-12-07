package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.services.exceptions.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final String PROPERTY_NULL_OR_EMPTY = "NULL OR EMPTY PROPERTY";

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return repository.findAll().stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category response = repository.findById(id).orElseThrow( () -> new EntityNotFoundException("Category Not Found"));
        return new CategoryDTO(response);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category entity = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return new CategoryDTO(repository.save(entity));
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        Category updateCategory = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category Not Found"));

        if(categoryDTO.getName() == null || categoryDTO.getName().isBlank()) {
            throw new PropertyNotFoundException(PROPERTY_NULL_OR_EMPTY);
        }

        updateCategory.setName(categoryDTO.getName());
        return new CategoryDTO(repository.save(updateCategory));
    }
}
