package ma.abdellahelmoutaouakil.distributeurautomatique.controllers;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.services.ProductService;
import ma.abdellahelmoutaouakil.distributeurautomatique.services.VendingMachineService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private VendingMachineService vendingMachineService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }

        @Bean
        public VendingMachineService vendingMachineService() {
            return Mockito.mock(VendingMachineService.class);
        }
    }

    @Test
    void shouldReturnAllProducts() throws Exception {
        ProductDTO dto = new ProductDTO(1L, "Eau", 1.0f, true);
        when(productService.getAllProducts()).thenReturn(List.of(dto));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Eau")))
                .andExpect(jsonPath("$[0].purchasable", is(true)));
    }

    @Test
    void shouldReturnPurchasableProducts() throws Exception {
        ProductDTO dto1 = new ProductDTO(1L, "Eau", 1.0f, true);
        ProductDTO dto2 = new ProductDTO(2L, "Jus", 4.0f, false);
        when(productService.getPurchasableProducts(3.0f)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/products?amount=3.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].purchasable", is(true)))
                .andExpect(jsonPath("$[1].purchasable", is(false)));
    }
}
