package app.clip.expenses.services;

import app.clip.commons.exceptions.NotFoundException;
import app.clip.expenses.models.Expense;

import java.util.List;

public interface ExpenseService {

    Expense add(Expense expense);
    Expense update(Expense expense);
    Expense deleteById(final Long id) throws NotFoundException;
    Expense getById(final Long id) throws NotFoundException;
    List<Expense> fetchAllInGroup(final Long associatedGroupId);
    List<Expense> fetchWhereUserInvolved(final Long userId);
    List<Expense> fetchAll();
}
