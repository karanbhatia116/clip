package app.clip.splits.repositories;

import app.clip.splits.models.Split;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SplitRepository extends JpaRepository<Split, Long> {
    List<Split> findByExpenseId(Long expenseId);
    List<Split> findByUserId(Long userId);

    @Query("SELECT s from Split s where s.expenseId in :expenseIds")
    List<Split> findSplitsByExpenseIds(@Param("expenseIds") Collection<Long> expenseIds);
}
