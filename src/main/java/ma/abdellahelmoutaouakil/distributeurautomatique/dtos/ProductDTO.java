package ma.abdellahelmoutaouakil.distributeurautomatique.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private float price;
    private boolean purchasable;
}
