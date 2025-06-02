package ma.abdellahelmoutaouakil.distributeurautomatique.mappers;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionItemDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.TransactionItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface TransactionItemMapper {
    TransactionItemDTO toDTO(TransactionItem entity);
    TransactionItem fromDTO(TransactionItemDTO dto);
}
