package ma.abdellahelmoutaouakil.distributeurautomatique.repositories;

import ma.abdellahelmoutaouakil.distributeurautomatique.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
