package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;

import java.util.List;
import java.util.Map;

public interface VendingMachineService {
    float calculateTotal(List<Product> products);
    float calculateChange(float insertedAmount, float totalPrice);
    boolean canDistribute(List<Product> products, float insertedAmount);
    TransactionDTO distributeProducts(Transaction transaction, List<Product> products);
    boolean isProductPurchasable(Product product, float insertedAmount);
    void validateCoin(float coin);
    Map<Float, Integer> getChange(float inserted, float totalPrice);
}
