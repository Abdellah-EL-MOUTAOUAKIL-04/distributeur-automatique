package ma.abdellahelmoutaouakil.distributeurautomatique.controllers;

import lombok.AllArgsConstructor;
import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.services.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    // Lister tous les produits ou seulement ceux achetables avec un montant donné
    @GetMapping
    public List<ProductDTO> listProducts(@RequestParam(required = false) Float amount) {
        if (amount != null) {
            return productService.getPurchasableProducts(amount);
        }
        return productService.getAllProducts();
    }
}
