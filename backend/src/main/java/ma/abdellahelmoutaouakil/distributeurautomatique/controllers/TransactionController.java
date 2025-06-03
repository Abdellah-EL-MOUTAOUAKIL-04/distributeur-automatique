package ma.abdellahelmoutaouakil.distributeurautomatique.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionItemDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.TransactionItem;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.ProductMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionItemMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.services.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionController {

    private TransactionService transactionService;
    private ProductService productService;
    private TransactionItemService transactionItemService;
    private VendingMachineService vendingMachineService;

    private ProductMapper productMapper;
    private TransactionMapper transactionMapper;
    private TransactionItemMapper transactionItemMapper;

    /**
     * Crée une nouvelle transaction avec un montant initial inséré.
     */
    @PostMapping
    public TransactionDTO createTransaction(@RequestParam(defaultValue = "0.0") float insertedAmount) {
        System.out.println("insertedAmount ===> "+insertedAmount);
        return transactionService.createTransaction(insertedAmount);
    }

    /**
     * Ajoute une pièce à une transaction existante.
     */
    @PostMapping("/{id}/insert")
    public TransactionDTO insertCoin(@PathVariable Long id, @RequestParam float coin) {
        vendingMachineService.validateCoin(coin);
        Transaction transaction = transactionService.getById(id);
        transaction.setInsertedAmount(transaction.getInsertedAmount() + coin);
        return transactionMapper.toDTO(transactionService.update(transaction));
    }

    /**
     * Ajoute un produit à une transaction.
     */
    @PostMapping("/{id}/select")
    public ResponseEntity<?> selectProduct(
            @PathVariable Long id,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity) {

        Transaction transaction = transactionService.getById(id);
        ProductDTO productDTO = productService.getById(productId);
        Product product = productMapper.fromDTO(productDTO);

        if (!vendingMachineService.isProductPurchasable(product, transaction.getInsertedAmount())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Fonds insuffisants pour ce produit."));
        }

        TransactionItemDTO itemDTO = transactionItemService.addItem(transaction, product, quantity);
        return ResponseEntity.ok(itemDTO);
    }

    /**
     * Finalise la transaction : distribue les produits et rend la monnaie.
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmTransaction(@PathVariable Long id) {
        Transaction transaction = transactionService.getById(id);

        List<Product> selectedProducts = transaction.getItems().stream()
                .map(TransactionItem::getProduct)
                .toList();

        TransactionDTO finalizedTransaction = vendingMachineService.distributeProducts(transaction, selectedProducts);

        Map<Float, Integer> change = vendingMachineService.getChange(
                transaction.getInsertedAmount(),
                vendingMachineService.calculateTotal(selectedProducts)
        );

        return ResponseEntity.ok(Map.of(
                "status", "Produits distribués",
                "transaction", finalizedTransaction,
                "change", change
        ));
    }

    /**
     * Annule la transaction et retourne l'argent inséré.
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelTransaction(@PathVariable Long id) {
        Transaction transaction = transactionService.getById(id);
        transactionService.cancelTransaction(transaction);
        TransactionDTO cancelled = transactionMapper.toDTO(transaction);

        return ResponseEntity.ok(Map.of(
                "status", "Transaction annulée",
                "remboursement", transaction.getChangeGiven(),
                "transaction", cancelled
        ));
    }
}
