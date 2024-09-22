package app.clip.splits.services;

import app.clip.commons.exceptions.NotFoundException;
import app.clip.splits.models.Split;

import java.util.Collection;
import java.util.List;

public interface SplitService {

    Split create(Split split);
    Split deleteById(final Long id) throws NotFoundException;
    Split update(Split split);
    Split getById(final Long id) throws NotFoundException;
    Collection<Split> saveMultiple(Collection<Split> splits);
    Collection<Split> fetchAllSplitsForExpense(final Long expenseId);
    Collection<Split> findByUserId(final Long userId);
    List<Split> fetchSplitsForExpenses(Collection<Long> expenseIds);
}
