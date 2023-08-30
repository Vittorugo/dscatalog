package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.services.exceptions.PropertyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    private static final String PROPERTY_NULL_OR_EMPTY = "NULL OR EMPTY PROPERTY";

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        log.info("--- Início do método UserService.findAll ---");
        Page<User> list = repository.findAll(pageable);
        log.info("--- Lista de todas as categorias: " + list.getContent());
        log.info("--- Final do método UserService.findAll ---");
        return list.map(UserDTO::parseToDTO);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User response = repository.findById(id).orElseThrow( () -> new EntityNotFoundException("User Not Found"));
        return UserDTO.parseToDTO(response);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        var entity = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        entity.getRoles().clear();
        dto.getRoles().forEach( roleDTO -> {
            var role = roleRepository.getReferenceById(roleDTO.getId());
            entity.getRoles().add(role);
        });

        return UserDTO.parseToDTO(repository.save(entity));
    }

    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) {
        // Utilizando o findById
        //User updateUser = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        if(userDTO.getFirstName() == null || userDTO.getLastName().isBlank()) {
            throw new PropertyNotFoundException(PROPERTY_NULL_OR_EMPTY);
        }

        try {
            //Utilizando o getReferenceById
            User updateUser = repository.getReferenceById(id);
            updateUser.setFirstName(userDTO.getFirstName());
            updateUser.setLastName(userDTO.getLastName());
            updateUser.setEmail(userDTO.getEmail());

            return UserDTO.parseToDTO(repository.save(updateUser));
        } catch (javax.persistence.EntityNotFoundException exception) {
            throw new EntityNotFoundException("Entity Not Found");
        }
    }

    public void delete(Long id) {
        try{
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new EntityNotFoundException("User ID Not Found");
        } catch (DataIntegrityViolationException exception) {
            throw new DataBaseException("Integrity violation");
        }
    }
}
