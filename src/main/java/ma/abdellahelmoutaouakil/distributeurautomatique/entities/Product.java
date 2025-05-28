package ma.abdellahelmoutaouakil.distributeurautomatique.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Le nom du produit ne peut pas être vide")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "Le prix ne peut pas être null")
    @Column(nullable = false)
    private float price;
}
