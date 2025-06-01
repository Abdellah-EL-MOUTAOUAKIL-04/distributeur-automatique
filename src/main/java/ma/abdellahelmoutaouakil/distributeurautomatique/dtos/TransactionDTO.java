package ma.abdellahelmoutaouakil.distributeurautomatique.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private float insertedAmount;
    private float changeGiven;
    private String status;
    private List<TransactionItemDTO> items;
}
