package ma.abdellahelmoutaouakil.distributeurautomatique.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TransactionDTO {
    private Long id;
    private float insertedAmount;
    private float changeGiven;
    private String status; // TransactionStatus as String (e.g., "PENDING")
    private List<TransactionItemDTO> items;
}
