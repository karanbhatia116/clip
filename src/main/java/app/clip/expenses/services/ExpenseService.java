package app.clip.expenses.services;

import app.clip.expenses.models.Expense;

import java.util.List;

public interface ExpenseService {

    Expense add(Expense expense);
    Expense update(Expense expense);
    Expense deleteById(final Long id);
    Expense getById(final Long id);
    List<Expense> fetchAllInGroup(final Long associatedGroupId);
    List<Expense> fetchWhereUserInvolved(final Long userId);
    List<Expense> fetchAll();
}
