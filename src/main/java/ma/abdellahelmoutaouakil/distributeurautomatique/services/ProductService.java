package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getAllProducts();
    public List<Product> getPurchasableProducts(float availableAmount);
    public Product getById(Long id);
    public Product save(Product product);
    public void delete(Long id);
}
