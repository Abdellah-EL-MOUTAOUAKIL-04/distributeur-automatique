package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionItemDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.TransactionItem;

public interface TransactionItemService {
    public TransactionItemDTO addItem(Transaction transaction, Product product, int quantity);
}
