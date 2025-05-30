package ma.abdellahelmoutaouakil.distributeurautomatique.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Produit avec l'ID " + id + " introuvable");
    }
}
