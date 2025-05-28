package ma.abdellahelmoutaouakil.distributeurautomatique.mappers;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.ProductDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Product;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "purchasable", expression = "java(product.isPurchasable(availableAmount))")
    ProductDTO toDTO(Product product, @Context float availableAmount);
    Product fromDTO(ProductDTO dto);
}
