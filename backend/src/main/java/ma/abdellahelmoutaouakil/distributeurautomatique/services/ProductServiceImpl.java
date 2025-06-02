package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.ProductNotFoundException;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.ProductMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private ProductMapper productMapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getPurchasableProducts(float availableAmount) {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> {
                    ProductDTO dto = productMapper.toDTO(product);
                    dto.setPurchasable(product.getPrice() <= availableAmount); // 👍 ici le seul champ dynamique
                    return dto;
                })
                .toList();
    }

    @Override
    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product product = productMapper.fromDTO(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
