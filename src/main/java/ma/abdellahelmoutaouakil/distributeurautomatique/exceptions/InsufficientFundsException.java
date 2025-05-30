package ma.abdellahelmoutaouakil.distributeurautomatique.exceptions;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Fonds insuffisants pour effectuer l’achat");
    }
}
