package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;

public interface TransactionService {
    public Transaction createTransaction(float insertedAmount);
    public Transaction getById(Long id);
    public void cancelTransaction(Transaction transaction);
    public void finalizeTransaction(Transaction transaction, float totalPrice);
}
