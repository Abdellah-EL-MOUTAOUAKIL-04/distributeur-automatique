package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getPurchasableProducts(float availableAmount);
    ProductDTO getById(Long id);
    ProductDTO save(ProductDTO productDTO);
    void delete(Long id);
}
