package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.enums.TransactionStatus;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.TransactionNotFoundException;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private TransactionMapper transactionMapper;

    @Override
    public TransactionDTO createTransaction(float insertedAmount) {
        log.info("Creating new transaction with amount: {}", insertedAmount);
        Transaction transaction = new Transaction();
        transaction.setInsertedAmount(insertedAmount);
        transaction.setStatus(TransactionStatus.PENDING);
        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.toDTO(saved);
    }

    @Override
    public Transaction getById(Long id) {
        log.info("Fetching transaction with ID: {}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Override
    public void cancelTransaction(Transaction transaction) {
        log.warn("Cancelling transaction ID: {}", transaction.getId());
        transaction.setStatus(TransactionStatus.CANCELLED);
        transaction.setChangeGiven(transaction.getInsertedAmount());
        transaction.getItems().clear(); // facultatif selon le comportement souhaité
        transactionRepository.save(transaction);
    }

    @Override
    public void finalizeTransaction(Transaction transaction, float totalPrice) {
        log.info("Finalizing transaction ID: {} with totalPrice: {}", transaction.getId(), totalPrice);
        transaction.setChangeGiven(transaction.getInsertedAmount() - totalPrice);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
    }
    @Override
    public Transaction update(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
