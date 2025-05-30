package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import lombok.RequiredArgsConstructor;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.InsufficientFundsException;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.InvalidCoinException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendingMachineServiceImpl implements VendingMachineService {

    private final TransactionService transactionService;

    private static final List<Float> VALID_COINS = List.of(0.5f, 1f, 2f, 5f, 10f);

    public void validateCoin(float coin) {
        if (!VALID_COINS.contains(coin)) {
            throw new InvalidCoinException(coin);
        }
    }

    public float calculateTotal(List<Product> products) {
        return (float) products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public float calculateChange(float inserted, float totalPrice) {
        return inserted - totalPrice;
    }

    public boolean canDistribute(List<Product> products, float insertedAmount) {
        float total = calculateTotal(products);
        return insertedAmount >= total;
    }

    public void distributeProducts(Transaction transaction, List<Product> products) {
        float total = calculateTotal(products);
        if (transaction.getInsertedAmount() < total) {
            throw new InsufficientFundsException();
        }
        transactionService.finalizeTransaction(transaction, total);
    }

    public boolean isProductPurchasable(Product product, float insertedAmount) {
        return product.getPrice() <= insertedAmount;
    }
}
