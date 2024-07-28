package app.clip.expenses.adapters;

import app.clip.expenses.dtos.ExpenseDTO;
import app.clip.expenses.models.Expense;
import app.clip.splits.models.Split;

import java.util.Collection;

public class ExpenseDTOAdapter {

    public static Expense toExpense(ExpenseDTO expenseDTO) {
        if (expenseDTO == null) return null;
        Expense expense = new Expense();
        expense.setDescription(expenseDTO.description());
        expense.setCreatorId(expenseDTO.creatorId());
        expense.setTitle(expenseDTO.title());
        expense.setPayerId(expenseDTO.payerId());
        expense.setTotalAmount(expenseDTO.totalAmount());
        expense.setAssociatedGroupId(expenseDTO.associatedGroupId());
        if (expenseDTO.id() != null) {
            expense.setId(expenseDTO.id());
        }

        return expense;
    }

    public static ExpenseDTO toExpenseDTO(Expense expense, Collection<Split> splits) {
        if (expense == null) return null;

        return new ExpenseDTO(
                expense.getId(),
                expense.getCreatorId(),
                expense.getPayerId(),
                expense.getTotalAmount(),
                expense.getTitle(),
                expense.getDescription(),
                splits.stream().toList(),
                expense.getAssociatedGroupId()
        );
    }
}
