package by.fin.example.controlles;

import by.fin.example.dto.PhotoDTO;
import by.fin.example.dto.ProductDTO;
import by.fin.example.facades.ImageIntegrationFacade;
import by.fin.example.facades.ProductFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static by.fin.example.constants.FinByConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductFacade productFacade;

    @MockBean
    private ImageIntegrationFacade imageIntegrationFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setBrand("Test Brand");
        productDTO.setModel("Test Model");
        productDTO.setQuantityAvailable(10);
        productDTO.setWeight(1.0);
        productDTO.setRating(4.5);
        productDTO.setCategory("Test Category");
        productDTO.setColor("Black");
        productDTO.setDescription("Test Description");
        productDTO.setPrice(100.0);
        productDTO.setFeatures(Collections.singletonList("Feature1"));
    }

    @Test
    void whenGetProducts_thenStatus200() throws Exception {
        when(productFacade.findAll()).thenReturn(Collections.singletonList(productDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(API + PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(productDTO))))
                .andDo(print());

        verify(productFacade, times(1)).findAll();
    }

    @Test
    void whenGetProduct_thenStatus200() throws Exception {
        when(productFacade.findById(1L)).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(API + PRODUCTS + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productDTO)))
                .andDo(print());

        verify(productFacade, times(1)).findById(1L);
    }

    @Test
    void whenCreateProduct_thenStatus201() throws Exception {
        when(productFacade.save(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(API + PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(productDTO)))
                .andDo(print());

        verify(productFacade, times(1)).save(productDTO);
    }

    @Test
    void whenProductExist_thenStatus200andUpdate() throws Exception {
        when(productFacade.findById(1L)).thenReturn(productDTO);
        when(productFacade.update(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(API + PRODUCTS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productDTO)))
                .andDo(print());

        verify(productFacade, times(1)).update(productDTO);
    }

    @Test
    void whenProductNotExist_thenStatus201andSave() throws Exception {
        when(productFacade.findById(1L)).thenReturn(null);
        when(productFacade.save(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(API + PRODUCTS + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(productDTO)))
                .andDo(print());

        verify(productFacade, times(1)).save(productDTO);
    }

    @Test
    void whenDeleteProduct_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API + PRODUCTS + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(productFacade, times(1)).delete(1L);
    }

    @Test
    void whenRemoveBackground_thenStatus200() throws Exception {
        when(productFacade.findById(1L)).thenReturn(productDTO);
        when(imageIntegrationFacade.removeBackground(any(PhotoDTO.class), any(ProductDTO.class))).thenReturn(productDTO);

        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setUrl("photoUrl");

        mockMvc.perform(MockMvcRequestBuilders.put(API + PRODUCTS + REMOVE_BACKGROUND + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(photoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productDTO)))
                .andDo(print());

        verify(productFacade, times(1)).findById(1L);
        verify(imageIntegrationFacade, times(1)).removeBackground(photoDTO, productDTO);
    }

    @Test
    void whenGetHighestRatingProduct_thenStatus200() throws Exception {
        when(productFacade.findHighestRatingProduct()).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(API + PRODUCTS + HIGHEST_RATING_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productDTO)))
                .andDo(print());

        verify(productFacade, times(1)).findHighestRatingProduct();
    }

    @Test
    void whenGetMostExpensiveProduct_thenStatus200() throws Exception {
        when(productFacade.findMostExpensiveProduct()).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(API + PRODUCTS + MOST_EXPENSIVE_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productDTO)))
                .andDo(print());

        verify(productFacade, times(1)).findMostExpensiveProduct();
    }

    @Test
    void whenGetCheapestProduct_thenStatus200() throws Exception {
        when(productFacade.findCheapestProduct()).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(API + PRODUCTS + CHEAPEST_PRODUCT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productDTO)))
                .andDo(print());

        verify(productFacade, times(1)).findCheapestProduct();
    }
}