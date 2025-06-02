package ma.abdellahelmoutaouakil.distributeurautomatique.services;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.exceptions.ProductNotFoundException;
import ma.abdellahelmoutaouakil.distributeurautomatique.mappers.ProductMapper;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getPurchasableProducts_shouldMarkPurchasableCorrectly() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Eau");
        p1.setPrice(1.0f);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Jus");
        p2.setPrice(4.0f);

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));
        when(productMapper.toDTO(any())).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            return new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getPrice() <= 2.0f);
        });

        List<ProductDTO> result = productService.getPurchasableProducts(2.0f);

        assertEquals(2, result.size());
        assertTrue(result.get(0).isPurchasable());
        assertFalse(result.get(1).isPurchasable());
    }

    @Test
    void getAllProducts_shouldReturnAllProductDTOs() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Eau");
        product.setPrice(1.0f);

        ProductDTO dto = new ProductDTO(1L, "Eau", 1.0f, false);

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDTO(product)).thenReturn(dto);

        List<ProductDTO> result = productService.getAllProducts();
        assertEquals(1, result.size());
        assertEquals("Eau", result.get(0).getName());
    }

    @Test
    void getById_shouldReturnProductDTO() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Eau");
        product.setPrice(1.0f);

        ProductDTO dto = new ProductDTO(1L, "Eau", 1.0f, false);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(dto);

        ProductDTO result = productService.getById(1L);
        assertEquals("Eau", result.getName());
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getById(99L));
    }

    @Test
    void save_shouldReturnSavedProductDTO() {
        ProductDTO dto = new ProductDTO(null, "Eau", 1.0f, false);
        Product entity = new Product();
        entity.setName("Eau");
        entity.setPrice(1.0f);

        Product saved = new Product();
        saved.setId(1L);
        saved.setName("Eau");
        saved.setPrice(1.0f);

        when(productMapper.fromDTO(dto)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(saved);
        when(productMapper.toDTO(saved)).thenReturn(new ProductDTO(1L, "Eau", 1.0f, false));

        ProductDTO result = productService.save(dto);
        assertEquals(1L, result.getId());
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.delete(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowIfProductNotFound() {
        when(productRepository.existsById(99L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> productService.delete(99L));
    }
}