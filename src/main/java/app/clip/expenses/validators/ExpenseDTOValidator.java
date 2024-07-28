package app.clip.expenses.validators;

import app.clip.commons.Money;
import app.clip.commons.Violation;
import app.clip.expenses.dtos.ExpenseDTO;
import app.clip.splits.models.Split;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDTOValidator {

    public static List<Violation> validateExpenseDTO(ExpenseDTO expenseDTO) {
        List<Violation> violations = new ArrayList<>();
        if (expenseDTO == null) return violations;

        if (expenseDTO.creatorId() == null) {
            violations.add(new Violation("Expense cannot have empty creator!"));
            return violations;
        }

        if (expenseDTO.payerId() == null) {
            violations.add(new Violation("Expense cannot have empty payer!"));
            return violations;
        }

        violations.addAll(validateTotalAmt(expenseDTO.totalAmount()));
        violations.addAll(validateSplits(expenseDTO.splits(), expenseDTO.totalAmount()));
        return violations;
    }

    private static List<Violation> validateTotalAmt(Money totalAmt) {
        List<Violation> violations = new ArrayList<>();
        if (totalAmt == null || totalAmt.getAmount() == null) {
            violations.add(new Violation("Total amount cannot be null for expense!"));
            return violations;
        }
        if (totalAmt.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            violations.add(new Violation("Total amount cannot be negative!"));
            return violations;
        }
        return violations;
    }

    private static List<Violation> validateSplits(List<Split> splits, Money totalAmt) {
        List<Violation> violations = new ArrayList<>();
        if (splits == null || splits.isEmpty()) {
            violations.add(new Violation("Splits cannot be empty. Required parameter!"));
            return violations;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Split split : splits) {
            if (split.getUserId() == null) {
                violations.add(new Violation("Split can only exist for a user!"));
                return violations;
            }
            if (split.getSplitAmount() == null || split.getSplitAmount().getAmount() == null) {
                violations.add(new Violation("Split can only exist with a split amount!"));
                return violations;
            }
            if (split.getSplitAmount().getCurrency() != totalAmt.getCurrency()) {
                violations.add(new Violation("Split cannot be in currency different than the total amount"));
                return violations;
            }
            total = total.add(split.getSplitAmount().getAmount());
        }

        if (total.compareTo(BigDecimal.ZERO) != 0) {
            violations.add(new Violation("Invalid split amounts. Sum is not 0"));
        }

        return violations;
    }

}
