package ma.abdellahelmoutaouakil.distributeurautomatique.dtos;

import lombok.Data;

@Data
public class TransactionItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;
}
