package ma.abdellahelmoutaouakil.distributeurautomatique.mappers;

import ma.abdellahelmoutaouakil.distributeurautomatique.dtos.TransactionDTO;
import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TransactionItemMapper.class})
public interface TransactionMapper {
    TransactionDTO toDTO(Transaction transaction);
    Transaction fromDTO(TransactionDTO dto);
}
