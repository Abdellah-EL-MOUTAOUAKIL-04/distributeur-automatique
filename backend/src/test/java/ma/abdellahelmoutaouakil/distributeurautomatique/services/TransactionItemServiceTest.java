package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionItemDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.TransactionItem;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionItemMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.TransactionItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionItemServiceTest {

    @Mock
    private TransactionItemRepository transactionItemRepository;

    @Mock
    private TransactionItemMapper transactionItemMapper;

    @InjectMocks
    private TransactionItemServiceImpl transactionItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addItem_shouldSaveTransactionItemAndReturnDTO() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        Product product = new Product();
        product.setId(2L);

        TransactionItem item = new TransactionItem();
        item.setTransaction(transaction);
        item.setProduct(product);
        item.setQuantity(3);

        TransactionItem savedItem = new TransactionItem();
        savedItem.setId(10L);
        savedItem.setTransaction(transaction);
        savedItem.setProduct(product);
        savedItem.setQuantity(3);

        TransactionItemDTO dto = new TransactionItemDTO();
        dto.setId(10L);
        dto.setQuantity(3);

        when(transactionItemRepository.save(any())).thenReturn(savedItem);
        when(transactionItemMapper.toDTO(savedItem)).thenReturn(dto);

        TransactionItemDTO result = transactionItemService.addItem(transaction, product, 3);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(3, result.getQuantity());
    }
}
