package ma.abdellahelmoutaouakil.distributeurautomatique.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Transaction avec l'ID " + id + " introuvable");
    }
}
