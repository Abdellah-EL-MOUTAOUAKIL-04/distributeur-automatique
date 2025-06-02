package ma.abdellahelmoutaouakil.distributeurautomatique;

import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import ma.abdellahelmoutaouakil.distributeurautomatique.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class DistributeurAutomatiqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeurAutomatiqueApplication.class, args);
    }
    @Bean
    @Profile("!test")
    public CommandLineRunner initProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(new Product(null, "Soda", 3.5f));
                productRepository.save(new Product(null, "TikTak", 2.0f));
                productRepository.save(new Product(null, "Eau", 1.0f));
                productRepository.save(new Product(null, "Jus", 4.0f));
                System.out.println("✅ Produits initiaux insérés !");
            }
        };
    }
}
