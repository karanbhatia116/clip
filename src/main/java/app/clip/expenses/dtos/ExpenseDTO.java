package app.clip.expenses.dtos;

import app.clip.commons.money.Money;
import app.clip.splits.models.Split;

import java.util.List;

public record ExpenseDTO (
        Long id,
        Long creatorId,
        Long payerId,
        Money totalAmount,
        String title,
        String description,
        List<Split> splits,
        Long associatedGroupId
) {
}
