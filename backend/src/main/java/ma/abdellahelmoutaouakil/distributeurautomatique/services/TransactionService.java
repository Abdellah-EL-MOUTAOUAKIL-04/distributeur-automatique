package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;

public interface TransactionService {
    TransactionDTO createTransaction(float insertedAmount);
    Transaction getById(Long id);
    void cancelTransaction(Transaction transaction);
    void finalizeTransaction(Transaction transaction, float totalPrice);
    public Transaction update(Transaction transaction);
}
