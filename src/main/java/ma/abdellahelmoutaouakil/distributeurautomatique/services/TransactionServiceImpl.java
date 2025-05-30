package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import lombok.RequiredArgsConstructor;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.enums.TransactionStatus;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.TransactionNotFoundException;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction createTransaction(float insertedAmount) {
        Transaction transaction = new Transaction();
        transaction.setInsertedAmount(insertedAmount);
        transaction.setStatus(TransactionStatus.PENDING);
        return transactionRepository.save(transaction);
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public void cancelTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.CANCELLED);
        transaction.setChangeGiven(transaction.getInsertedAmount());
        transaction.getItems().clear();
        transactionRepository.save(transaction);
    }

    public void finalizeTransaction(Transaction transaction, float totalPrice) {
        transaction.setChangeGiven(transaction.getInsertedAmount() - totalPrice);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
    }
}
