package ma.abdellahelmoutaouakil.distributeurautomatique.utils;

import java.util.*;

public class ChangeCalculator {

    private static final List<Float> VALID_COINS = CoinValidator.getValidCoins()
            .stream()
            .sorted(Comparator.reverseOrder()) // On veut d'abord les plus grandes pièces
            .toList();

    /**
     * Calcule le rendu de la monnaie avec le minimum de pièces
     * @param change Montant à rendre (ex : 3.5 MAD)
     * @return une Map contenant les pièces et leurs quantités
     */
    public static Map<Float, Integer> calculateOptimalChange(float change) {
        Map<Float, Integer> changeMap = new LinkedHashMap<>();

        float remaining = change;
        for (float coin : VALID_COINS) {
            int count = (int) (remaining / coin);
            if (count > 0) {
                changeMap.put(coin, count);
                remaining = round(remaining - (coin * count));
            }
        }

        if (remaining > 0.0001f) {
            throw new IllegalStateException("Impossible de rendre exactement la monnaie avec les pièces disponibles.");
        }

        return changeMap;
    }

    private static float round(float value) {
        return (float) (Math.round(value * 100.0) / 100.0);
    }
}
