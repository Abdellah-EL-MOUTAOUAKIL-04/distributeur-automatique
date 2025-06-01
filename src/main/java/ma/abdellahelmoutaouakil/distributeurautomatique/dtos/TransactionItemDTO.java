package ma.abdellahelmoutaouakil.distributeurautomatique.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;
}
