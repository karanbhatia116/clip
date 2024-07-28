package app.clip.transactions.repositories;

import app.clip.transactions.models.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByLenderIdAndBorrowerId(Long lenderId, Long borrowerId);
    List<Transaction> findByLenderId(Long lenderId);
    List<Transaction> findByBorrowerId(Long borrowerId);
    List<Transaction> findByAssociatedGroupId(Long associatedGroupId);

    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.isActive = false where t.id = :id")
    void markAsInactive(@Param("id") Long id);
}
