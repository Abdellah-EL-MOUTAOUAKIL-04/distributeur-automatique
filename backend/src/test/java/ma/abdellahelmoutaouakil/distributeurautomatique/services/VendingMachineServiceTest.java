package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.enums.TransactionStatus;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.InsufficientFundsException;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VendingMachineServiceTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private VendingMachineServiceImpl vendingMachineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateTotal_shouldSumPrices() {
        Product p1 = new Product();
        p1.setPrice(2f);
        Product p2 = new Product();
        p2.setPrice(3f);

        float total = vendingMachineService.calculateTotal(List.of(p1, p2));
        assertEquals(5f, total);
    }

    @Test
    void calculateChange_shouldReturnCorrectDifference() {
        float change = vendingMachineService.calculateChange(10f, 6.5f);
        assertEquals(3.5f, change);
    }

    @Test
    void canDistribute_shouldReturnTrueWhenEnoughMoney() {
        Product p = new Product();
        p.setPrice(2f);
        assertTrue(vendingMachineService.canDistribute(List.of(p), 2f));
    }

    @Test
    void canDistribute_shouldReturnFalseWhenNotEnough() {
        Product p = new Product();
        p.setPrice(5f);
        assertFalse(vendingMachineService.canDistribute(List.of(p), 3f));
    }

    @Test
    void isProductPurchasable_shouldReturnTrue() {
        Product p = new Product();
        p.setPrice(1.5f);
        assertTrue(vendingMachineService.isProductPurchasable(p, 2f));
    }

    @Test
    void isProductPurchasable_shouldReturnFalse() {
        Product p = new Product();
        p.setPrice(3f);
        assertFalse(vendingMachineService.isProductPurchasable(p, 2f));
    }

    @Test
    void distributeProducts_shouldThrowIfInsufficientFunds() {
        Transaction transaction = new Transaction();
        transaction.setInsertedAmount(1f);
        Product p = new Product();
        p.setPrice(2f);
        assertThrows(InsufficientFundsException.class, () ->
                vendingMachineService.distributeProducts(transaction, List.of(p)));
    }

    @Test
    void distributeProducts_shouldFinalizeTransactionAndReturnDTO() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setInsertedAmount(5f);
        Product p = new Product();
        p.setPrice(2f);

        TransactionDTO dto = new TransactionDTO();
        dto.setId(1L);

        doNothing().when(transactionService).finalizeTransaction(transaction, 2f);
        when(transactionMapper.toDTO(transaction)).thenReturn(dto);

        TransactionDTO result = vendingMachineService.distributeProducts(transaction, List.of(p));

        assertEquals(1L, result.getId());
        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        assertEquals(3f, transaction.getChangeGiven());
    }

    @Test
    void getChange_shouldReturnOptimalChange() {
        Map<Float, Integer> change = vendingMachineService.getChange(10f, 7.5f);
        assertEquals(1, change.get(2f));
        assertEquals(1, change.get(0.5f));
    }
}
