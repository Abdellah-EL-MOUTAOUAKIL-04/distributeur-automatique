package ma.abdellahelmoutaouakil.distributeurautomatique.exceptions;

public class InvalidCoinException extends RuntimeException {
    public InvalidCoinException(float value) {
        super("Pièce de valeur " + value + " MAD non acceptée");
    }
}
