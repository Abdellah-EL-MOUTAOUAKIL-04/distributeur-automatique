package ma.abdellahelmoutaouakil.distributeurautomatique.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private float price;
    private boolean purchasable;
}
