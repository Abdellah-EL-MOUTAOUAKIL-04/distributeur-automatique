package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.TransactionItem;

import java.util.List;
import java.util.Map;

public interface VendingMachineService {
    float calculateTotal(List<TransactionItem> transactionItems);

    float calculateChange(float insertedAmount, float totalPrice);

    boolean canDistribute(List<TransactionItem> transactionItems, float insertedAmount);

    TransactionDTO distributeProducts(Transaction transaction, List<TransactionItem> transactionItems);

    boolean isProductPurchasable(Product product,int quantity, float insertedAmount);

    void validateCoin(float coin);

    Map<Float, Integer> getChange(float inserted, float totalPrice);

}
