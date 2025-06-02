package ma.abdellahelmoutaouakil.distributeurautomatique.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;
}
