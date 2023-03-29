package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.factories.ProductFactory;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private final static String BASE_URL_WITH_ID = "/products/{id}";

    @Autowired
    private ObjectMapper objectMapper;

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

        ResultActions resultActions = mockMvc.perform(get(BASE_URL_WITH_ID,existsId)
                .accept("application/json"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").value("Agua"));
        resultActions.andExpect(jsonPath("categories[0].name").value("TECH"));
        resultActions.andExpect(jsonPath("categories[?(@.name == 'GAME')].name").value("GAME"));
    }

    @Test
    public void findByIdShouldReturnNoFoundExceptionWhenIdDoesNotExist() throws Exception {
        when(productService.findById(nonExistsId)).thenThrow(EntityNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL_WITH_ID, nonExistsId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldUpdateProductWhenIdExist() throws Exception {
        // Quando utilizamos o any() do ArgumentMatchers não podemos utlizar tbm variáveis de tipo simples.
        // Devemos utilizar o método eq('variável_aqui_dentro') para o código compilar.
        when(productService.update(any(), eq(existsId))).thenReturn(dto);

        var productJsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = mockMvc.perform(put(BASE_URL_WITH_ID, existsId)
                        .content(productJsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json"));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.name").value("Agua"));
        resultActions.andExpect(jsonPath("categories[?(@.name == 'TECH')].name").value("TECH"));
    }

    @Test
    public void updateShouldThrowExceptionWhenIdDoesNotExist() throws Exception {
        when(productService.update(any(), eq(nonExistsId))).thenThrow(EntityNotFoundException.class);

        var productJsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = mockMvc.perform(put(BASE_URL_WITH_ID, nonExistsId)
                .content(productJsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExist() throws Exception {
        //doNothing().when(productService).delete(existsId); // Essa forma seria utilizada caso o método delete fosse void.
        when(productService.delete(existsId)).thenReturn("Product deleted");

        ResultActions resultActions = mockMvc.perform(delete(BASE_URL_WITH_ID, existsId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andReturn().getResponse().getContentAsString().equals("Product deleted");
    }

    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExist() throws Exception {
        when(productService.delete(nonExistsId)).thenThrow(EntityNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(delete(BASE_URL_WITH_ID, nonExistsId));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldThrowExceptionWhenDataIntegrityViolation() throws Exception {
        when(productService.delete(nonExistsId)).thenThrow(DataBaseException.class);

        ResultActions resultActions = mockMvc.perform(delete(BASE_URL_WITH_ID, nonExistsId));

        resultActions.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void insertShouldCreateObject() throws Exception{
        when(productService.insert(any())).thenReturn(dto);

        var productJsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = mockMvc.perform(post("/products")
                        .content(productJsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.name").value("Agua"));
        resultActions.andExpect(jsonPath("$.categories[1].name").value("GAME"));
        resultActions.andExpect(jsonPath("$.categories[?(@.name == 'TECH')].id").value(1));
    }
}
