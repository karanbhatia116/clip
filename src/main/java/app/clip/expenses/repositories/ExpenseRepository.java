package app.clip.expenses.repositories;

import app.clip.expenses.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByAssociatedGroupId(Long associatedGroupId);
}
