package ma.abdellahelmoutaouakil.distributeurautomatique.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.services.ProductService;
import ma.abdellahelmoutaouakil.distributeurautomatique.services.VendingMachineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;
    private ProductService productService;
    private VendingMachineService vendingMachineService;

    @BeforeEach
    void setUp() {
        productService = Mockito.mock(ProductService.class);
        vendingMachineService = Mockito.mock(VendingMachineService.class);
        ProductController controller = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
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
