package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;

import java.util.List;
import java.util.Map;

public interface VendingMachineService {
    public void validateCoin(float coin);
    public float calculateTotal(List<Product> products);
    public float calculateChange(float inserted, float totalPrice);
    Map<Float, Integer> getOptimizedChange(float inserted, float totalPrice);
    public boolean canDistribute(List<Product> products, float insertedAmount);
    public void distributeProducts(Transaction transaction, List<Product> products);
    public boolean isProductPurchasable(Product product, float insertedAmount);
}
