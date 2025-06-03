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

        // Chercher un item existant avec le même produit dans la transaction
        TransactionItem existingItem = transaction.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        TransactionItem itemToSave;

        if (existingItem != null) {
            // Le produit existe déjà : on met à jour la quantité
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            itemToSave = existingItem;
            log.info("Updated quantity of existing item");
        } else {
            // Nouveau produit : on le crée
            TransactionItem newItem = new TransactionItem();
            newItem.setTransaction(transaction);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            itemToSave = newItem;
            log.info("Created new transaction item");
        }

        TransactionItem saved = transactionItemRepository.save(itemToSave);
        return transactionItemMapper.toDTO(saved);
    }
}
