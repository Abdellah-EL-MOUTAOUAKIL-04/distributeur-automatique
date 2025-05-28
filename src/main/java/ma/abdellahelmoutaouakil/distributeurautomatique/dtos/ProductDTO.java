package ma.abdellahelmoutaouakil.distributeurautomatique.dtos;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private float price;
    private boolean purchasable;
}
