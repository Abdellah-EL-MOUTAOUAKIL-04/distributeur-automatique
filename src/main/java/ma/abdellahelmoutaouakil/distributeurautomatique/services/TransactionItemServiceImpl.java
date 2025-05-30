package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import lombok.RequiredArgsConstructor;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.TransactionItem;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.TransactionItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionItemServiceImpl implements TransactionItemService {

    private final TransactionItemRepository transactionItemRepository;

    public TransactionItem addItem(Transaction transaction, Product product, int quantity) {
        TransactionItem item = new TransactionItem();
        item.setTransaction(transaction);
        item.setProduct(product);
        item.setQuantity(quantity);
        return transactionItemRepository.save(item);
    }
}
