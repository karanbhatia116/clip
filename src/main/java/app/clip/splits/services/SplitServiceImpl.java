package app.clip.splits.services;

import app.clip.commons.constants.AssetClass;
import app.clip.commons.exceptions.NotFoundException;
import app.clip.splits.models.Split;
import app.clip.splits.repositories.SplitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SplitServiceImpl implements SplitService {

    private final SplitRepository splitRepository;

    public SplitServiceImpl(SplitRepository splitRepository) {
        this.splitRepository = splitRepository;
    }

    @Override
    @Transactional
    public Split create(Split split) {
        return splitRepository.saveAndFlush(split);
    }

    @Override
    @Transactional
    public Split deleteById(Long id) throws NotFoundException {
        Split split = getById(id);
        splitRepository.deleteById(split.getId());
        return split;
    }

    @Override
    @Transactional
    public Split update(Split split) {
        return splitRepository.saveAndFlush(split);
    }

    @Override
    public Split getById(Long id) throws NotFoundException {
        return splitRepository.findById(id).orElseThrow(() -> new NotFoundException(AssetClass.SPLIT.name(), id.toString()));
    }

    @Override
    @Transactional
    public Collection<Split> saveMultiple(Collection<Split> splits) {
        return splitRepository.saveAll(splits);
    }

    @Override
    public Collection<Split> fetchAllSplitsForExpense(Long expenseId) {
        return splitRepository.findByExpenseId(expenseId);
    }

    @Override
    public Collection<Split> findByUserId(final Long userId) {
        return splitRepository.findByUserId(userId);
    }

    @Override
    public List<Split> fetchSplitsForExpenses(Collection<Long> expenseIds) {
        return splitRepository.findSplitsByExpenseIds(expenseIds);
    }


}
