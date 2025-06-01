package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import lombok.RequiredArgsConstructor;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.InsufficientFundsException;
import ma.abdellahelmoutaouakil.distributeurautomatique.utils.CoinValidator;
import ma.abdellahelmoutaouakil.distributeurautomatique.utils.ChangeCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VendingMachineServiceImpl implements VendingMachineService {

    private final TransactionService transactionService;

    @Override
    public float calculateTotal(List<Product> products) {
        return (float) products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    @Override
    public float calculateChange(float inserted, float totalPrice) {
        return inserted - totalPrice;
    }

    @Override
    public boolean canDistribute(List<Product> products, float insertedAmount) {
        float total = calculateTotal(products);
        return insertedAmount >= total;
    }

    @Override
    public void distributeProducts(Transaction transaction, List<Product> products) {
        float total = calculateTotal(products);
        if (transaction.getInsertedAmount() < total) {
            throw new InsufficientFundsException();
        }
        transactionService.finalizeTransaction(transaction, total);
    }

    @Override
    public boolean isProductPurchasable(Product product, float insertedAmount) {
        return product.getPrice() <= insertedAmount;
    }

    @Override
    public void validateCoin(float coin) {
        CoinValidator.validate(coin);
    }

    @Override
    public Map<Float, Integer> getOptimizedChange(float inserted, float totalPrice) {
        float change = calculateChange(inserted, totalPrice);
        return ChangeCalculator.calculateOptimalChange(change);
    }
}
