package app.clip.expenses.services;

import app.clip.commons.constants.AssetClass;
import app.clip.commons.exceptions.NotFoundException;
import app.clip.expenses.models.Expense;
import app.clip.expenses.repositories.ExpenseRepository;
import app.clip.splits.models.Split;
import app.clip.splits.services.SplitService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final SplitService splitService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, SplitService splitService) {
        this.expenseRepository = expenseRepository;
        this.splitService = splitService;
    }

    @Override
    @Transactional
    public Expense add(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public Expense update(Expense expense) {
        if (expense != null && expense.getId() != null) {
            expense.setUpdatedAt(System.currentTimeMillis());
        }
        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public Expense deleteById(Long id) throws NotFoundException {
        Expense expense = getById(id);
        expenseRepository.deleteById(id);
        return expense;
    }

    @Override
    public Expense getById(Long id) throws NotFoundException {
        return expenseRepository.findById(id).orElseThrow(() -> new NotFoundException(AssetClass.EXPENSE.name(), id.toString()));
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
