package app.clip.groups.services;

import app.clip.commons.constants.AssetClass;
import app.clip.commons.exceptions.NotFoundException;
import app.clip.transactions.models.Transaction;
import app.clip.transactions.services.TransactionService;
import app.clip.groups.models.Group;
import app.clip.groups.repositories.GroupRepository;
import app.clip.transactions.utils.TransactionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final TransactionService transactionService;

    public GroupServiceImpl(GroupRepository groupRepository, TransactionService transactionService) {
        this.groupRepository = groupRepository;
        this.transactionService = transactionService;
    }

    @Override
    public Group create(Group group) {
        return groupRepository.saveAndFlush(group);
    }

    @Override
    public Group update(Group group) {
        return groupRepository.saveAndFlush(group);
    }

    @Override
    public Group getById(Long id) throws NotFoundException {
        return groupRepository.findById(id).orElseThrow(() -> new NotFoundException(AssetClass.GROUP.name(), id.toString()));
    }

    @Override
    public Group deleteById(Long id) throws NotFoundException {
        Group group = getById(id);
        groupRepository.deleteById(id);
        return group;
    }

    @Override
    public Collection<Group> findByIds(Collection<Long> ids) {
        return groupRepository.findAllById(ids);
    }

    @Override
    public Group simplifyDebts(final Long id) throws NotFoundException {
        groupRepository.simplifyDebt(id);
        Collection<Transaction> transactionsWithinGroup = transactionService.findTransactionsWithinGroup(id);
        Collection<Transaction> simplifiedTransactions = TransactionUtils.simplifyTransactions(transactionsWithinGroup);

        for (Transaction transaction : transactionsWithinGroup) {
            transactionService.softDeleteById(transaction.getId());
        }
        transactionService.saveMultiple(simplifiedTransactions);
        return groupRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No group with id " + id + " found"));
    }
}
