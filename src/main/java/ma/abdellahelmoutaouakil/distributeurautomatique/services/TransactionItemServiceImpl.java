package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionItemDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.TransactionItem;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionItemMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.TransactionItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TransactionItemServiceImpl implements TransactionItemService {
    private TransactionItemRepository transactionItemRepository;
    private TransactionItemMapper transactionItemMapper;

    @Override
    public TransactionItemDTO addItem(Transaction transaction, Product product, int quantity) {
        log.info("Adding item to transaction [transactionId={}, productId={}, quantity={}]",
                transaction.getId(), product.getId(), quantity);

        TransactionItem item = new TransactionItem();
        item.setTransaction(transaction);
        item.setProduct(product);
        item.setQuantity(quantity);

        TransactionItem saved = transactionItemRepository.save(item);
        return transactionItemMapper.toDTO(saved);
    }
}
