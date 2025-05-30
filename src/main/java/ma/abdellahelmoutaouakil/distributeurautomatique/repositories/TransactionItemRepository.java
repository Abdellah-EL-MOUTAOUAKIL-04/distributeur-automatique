package ma.abdellahelmoutaouakil.distributeurautomatique.repositories;

import ma.abdellahelmoutaouakil.distributeurautomatique.entities.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {
}
