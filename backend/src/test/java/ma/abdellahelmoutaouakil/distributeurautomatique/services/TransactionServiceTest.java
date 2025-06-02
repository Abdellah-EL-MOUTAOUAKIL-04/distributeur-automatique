package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.enums.TransactionStatus;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.TransactionNotFoundException;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_shouldReturnDTO() {
        Transaction transaction = new Transaction();
        transaction.setInsertedAmount(5f);
        transaction.setStatus(TransactionStatus.PENDING);

        Transaction saved = new Transaction();
        saved.setId(1L);
        saved.setInsertedAmount(5f);
        saved.setStatus(TransactionStatus.PENDING);

        TransactionDTO dto = new TransactionDTO();
        dto.setId(1L);
        dto.setInsertedAmount(5f);
        dto.setStatus("PENDING");

        when(transactionRepository.save(any())).thenReturn(saved);
        when(transactionMapper.toDTO(saved)).thenReturn(dto);

        TransactionDTO result = transactionService.createTransaction(5f);
        assertEquals(5f, result.getInsertedAmount());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void getById_shouldReturnTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_shouldThrowException() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getById(99L));
    }

    @Test
    void cancelTransaction_shouldSetCancelledStatusAndChangeGiven() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setInsertedAmount(2.0f);
        transaction.setStatus(TransactionStatus.PENDING);

        transactionService.cancelTransaction(transaction);

        assertEquals(TransactionStatus.CANCELLED, transaction.getStatus());
        assertEquals(2.0f, transaction.getChangeGiven());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void finalizeTransaction_shouldSetCompletedStatusAndChange() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setInsertedAmount(5.0f);
        transaction.setStatus(TransactionStatus.PENDING);

        transactionService.finalizeTransaction(transaction, 3.0f);

        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        assertEquals(2.0f, transaction.getChangeGiven());
        verify(transactionRepository).save(transaction);
    }

    @Test
    void update_shouldSaveTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.update(transaction);
        assertEquals(1L, result.getId());
    }
}
