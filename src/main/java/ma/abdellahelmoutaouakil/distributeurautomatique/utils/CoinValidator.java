package ma.abdellahelmoutaouakil.distributeurautomatique.utils;

import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.InvalidCoinException;
import java.util.List;

public class CoinValidator {
    private static final List<Float> VALID_COINS = List.of(0.5f, 1f, 2f, 5f, 10f);

    public static void validate(float coin) {
        if (!VALID_COINS.contains(coin)) {
            throw new InvalidCoinException(coin);
        }
    }

    public static List<Float> getValidCoins() {
        return VALID_COINS;
    }
}
