package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.services.exceptions.PropertyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class CategoryService {

    private static final String PROPERTY_NULL_OR_EMPTY = "NULL OR EMPTY PROPERTY";

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        log.info("--- Início do método CategoryService.findAll ---");
        Page<Category> list = repository.findAll(pageable);
        log.info("--- Lista de todas as categorias: " + list.getContent());
        log.info("--- Final do método CategoryService.findAll ---");
        return list.map(CategoryDTO::new);
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
        // Utilizando o findById
        //Category updateCategory = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category Not Found"));

        if(categoryDTO.getName() == null || categoryDTO.getName().isBlank()) {
            throw new PropertyNotFoundException(PROPERTY_NULL_OR_EMPTY);
        }

        try {
            //Utilizando o getReferenceById
            Category updateCategory = repository.getReferenceById(id);
            updateCategory.setName(categoryDTO.getName());

            return new CategoryDTO(repository.save(updateCategory));
        } catch (javax.persistence.EntityNotFoundException exception) {
            throw new EntityNotFoundException("Entity Not Found");
        }
    }

    public void delete(Long id) {
        try{
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new EntityNotFoundException("Category ID Not Found");
        } catch (DataIntegrityViolationException exception) {
            throw new DataBaseException("Integrity violation");
        }
    }
}
