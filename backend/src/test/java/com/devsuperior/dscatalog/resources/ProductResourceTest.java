package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.factories.ProductFactory;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(ProductResource.class)
public class ProductResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private PageImpl<ProductDto> page;

    private ProductDto dto;
    private Long existsId;
    private Long nonExistsId;

    @BeforeEach
    void setUp() {
        existsId = 1L;
        nonExistsId = -999L;
        dto = ProductFactory.createProductDto();
        page = new PageImpl<>(List.of(dto));
    }

    @Test
    public void findAllTest() throws Exception {
        when(productService.findAll(any())).thenReturn(page);
        ResultActions resultActions =  mockMvc.perform(get("/products")
                .accept("application/json"));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturObjectWhenIdExist() throws Exception {
        when(productService.findById(existsId)).thenReturn(dto);

        ResultActions resultActions = mockMvc.perform(get("/products/{id}",existsId)
                .accept("application/json"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").value("Agua"));
        resultActions.andExpect(jsonPath("categories[0].name").value("TECH"));
        resultActions.andExpect(jsonPath("categories[?(@.name == 'GAME')].name").value("GAME"));
    }

    @Test
    public void findByIdShouldReturnNoFoundExceptionWhenIdNonExist() throws Exception {
        when(productService.findById(nonExistsId)).thenThrow(EntityNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get("/products/{id}", nonExistsId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }
}
