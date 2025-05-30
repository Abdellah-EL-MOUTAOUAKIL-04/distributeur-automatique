package ma.abdellahelmoutaouakil.distributeurautomatique.repositories;

import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
