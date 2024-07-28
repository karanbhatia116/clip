package app.clip.expenses.services;

import app.clip.expenses.models.Expense;
import app.clip.expenses.repositories.ExpenseRepository;
import app.clip.splits.models.Split;
import app.clip.splits.services.SplitService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final SplitService splitService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, SplitService splitService) {
        this.expenseRepository = expenseRepository;
        this.splitService = splitService;
    }

    @Override
    public Expense add(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Expense update(Expense expense) {
        if (expense != null && expense.getId() != null) {
            expense.setUpdatedAt(System.currentTimeMillis());
        }
        return expenseRepository.save(expense);
    }

    @Override
    public Expense deleteById(Long id) {
        Expense expense = getById(id);
        expenseRepository.deleteById(id);
        return expense;
    }

    @Override
    public Expense getById(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No expense found with id " + id));
    }

    @Override
    public List<Expense> fetchAllInGroup(Long associatedGroupId) {
        return expenseRepository.findByAssociatedGroupId(associatedGroupId);
    }

    @Override
    public List<Expense> fetchWhereUserInvolved(final Long userId) {
        Collection<Split> splits = splitService.findByUserId(userId);
        return expenseRepository.findAllById(splits.stream().map(Split::getExpenseId).toList());
    }

    @Override
    public List<Expense> fetchAll() {
        return expenseRepository.findAll();
    }
}
