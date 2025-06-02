package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import ma.abdellahelmoutaouakil.distributeurautomatique.enums.TransactionStatus;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.InsufficientFundsException;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.TransactionMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.utils.ChangeCalculator;
import ma.abdellahelmoutaouakil.distributeurautomatique.utils.CoinValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class VendingMachineServiceImpl implements VendingMachineService {

    private TransactionService transactionService;
    private TransactionMapper transactionMapper;

    @Override
    public float calculateTotal(List<Product> products) {
        float total = (float) products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        log.info("Calculated total price of products: {}", total);
        return total;
    }

    @Override
    public float calculateChange(float insertedAmount, float totalPrice) {
        float change = insertedAmount - totalPrice;
        log.info("Calculated change: inserted = {}, total = {}, change = {}", insertedAmount, totalPrice, change);
        return change;
    }

    @Override
    public boolean canDistribute(List<Product> products, float insertedAmount) {
        float total = calculateTotal(products);
        boolean result = insertedAmount >= total;
        log.info("Can distribute? insertedAmount = {}, total = {}, result = {}", insertedAmount, total, result);
        return result;
    }

    @Override
    public TransactionDTO distributeProducts(Transaction transaction, List<Product> products) {
        float total = calculateTotal(products);
        if (transaction.getInsertedAmount() < total) {
            throw new InsufficientFundsException();
        }

        transaction.setChangeGiven(calculateChange(transaction.getInsertedAmount(), total));
        transaction.setStatus(TransactionStatus.COMPLETED);

        transactionService.finalizeTransaction(transaction, total);
        log.info("Transaction completed: ID = {}, change = {}", transaction.getId(), transaction.getChangeGiven());

        return transactionMapper.toDTO(transaction);
    }

    @Override
    public boolean isProductPurchasable(Product product, float insertedAmount) {
        boolean purchasable = product.getPrice() <= insertedAmount;
        log.debug("Checking if product '{}' is purchasable with {} MAD: {}", product.getName(), insertedAmount, purchasable);
        return purchasable;
    }

    @Override
    public void validateCoin(float coin) {
        log.info("Validating inserted coin: {} MAD", coin);
        CoinValidator.validate(coin);
    }

    @Override
    public Map<Float, Integer> getChange(float inserted, float totalPrice) {
        float change = calculateChange(inserted, totalPrice);
        Map<Float, Integer> optimizedChange = ChangeCalculator.calculateOptimalChange(change);
        log.info("Optimized change breakdown: {}", optimizedChange);
        return optimizedChange;
    }
}
