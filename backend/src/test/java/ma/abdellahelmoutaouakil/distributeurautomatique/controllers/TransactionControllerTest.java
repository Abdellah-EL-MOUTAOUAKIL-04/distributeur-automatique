package ma.abdellahelmoutaouakil.distributeurautomatique.controllers;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionItemDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.ProductMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionItemMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.services.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTest {

    private MockMvc mockMvc;

    private TransactionService transactionService;
    private ProductService productService;
    private TransactionItemService transactionItemService;
    private VendingMachineService vendingMachineService;
    private ProductMapper productMapper;
    private TransactionMapper transactionMapper;
    private TransactionItemMapper transactionItemMapper;

    @BeforeEach
    void setup() {
        transactionService = mock(TransactionService.class);
        productService = mock(ProductService.class);
        transactionItemService = mock(TransactionItemService.class);
        vendingMachineService = mock(VendingMachineService.class);
        productMapper = mock(ProductMapper.class);
        transactionMapper = mock(TransactionMapper.class);
        transactionItemMapper = mock(TransactionItemMapper.class);

        TransactionController controller = new TransactionController(
                transactionService,
                productService,
                transactionItemService,
                vendingMachineService,
                productMapper,
                transactionMapper,
                transactionItemMapper
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testCreateTransaction() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        when(transactionService.createTransaction(0.0f)).thenReturn(dto);

        mockMvc.perform(post("/transactions"))
                .andExpect(status().isOk());
    }

    @Test
    void testInsertCoin() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setInsertedAmount(1.0f);
        transaction.setId(1L);

        TransactionDTO dto = new TransactionDTO();

        when(transactionService.getById(1L)).thenReturn(transaction);
        when(transactionService.update(any())).thenReturn(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(dto);

        mockMvc.perform(post("/transactions/1/insert?coin=1.0"))
                .andExpect(status().isOk());
    }

    @Test
    void testSelectProduct() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setInsertedAmount(3.0f);

        ProductDTO dto = new ProductDTO(1L, "Eau", 1.0f, true);
        Product product = new Product();
        product.setId(1L);
        product.setName("Eau");
        product.setPrice(1.0f);

        TransactionItemDTO itemDTO = new TransactionItemDTO();

        when(transactionService.getById(1L)).thenReturn(transaction);
        when(productService.getById(1L)).thenReturn(dto);
        when(productMapper.fromDTO(dto)).thenReturn(product);
        when(vendingMachineService.isProductPurchasable(product, 3.0f)).thenReturn(true);
        when(transactionItemService.addItem(transaction, product, 1)).thenReturn(itemDTO);

        mockMvc.perform(post("/transactions/1/select?productId=1&quantity=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setInsertedAmount(2.0f);
        transaction.setChangeGiven(2.0f);

        TransactionDTO dto = new TransactionDTO();

        when(transactionService.getById(1L)).thenReturn(transaction);
        doNothing().when(transactionService).cancelTransaction(transaction);
        when(transactionMapper.toDTO(transaction)).thenReturn(dto);

        mockMvc.perform(post("/transactions/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("Transaction annulée")))
                .andExpect(jsonPath("$.remboursement", is(2.0)));
    }
}
