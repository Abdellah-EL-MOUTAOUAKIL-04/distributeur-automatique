package ma.abdellahelmoutaouakil.distributeurautomatique.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ma.abdellahelmoutaouakil.distributeurautomatique.enums.TransactionStatus;

import java.util.List;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le montant inséré ne peut pas être null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être strictement positif")
    @Column(nullable = false)
    private float insertedAmount;

    private float changeGiven;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionItem> items;
}
