package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import lombok.RequiredArgsConstructor;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.ProductNotFoundException;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getPurchasableProducts(float availableAmount) {
        return productRepository.findAll().stream()
                .filter(p -> p.getPrice() <= availableAmount)
                .toList();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
