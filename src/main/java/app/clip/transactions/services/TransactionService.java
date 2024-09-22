package app.clip.transactions.services;

import app.clip.commons.exceptions.NotFoundException;
import app.clip.transactions.models.Transaction;

import java.util.Collection;

public interface TransactionService {

    Transaction save(Transaction transaction);
    Transaction getById(final Long id) throws NotFoundException;
    Transaction deleteById(final Long id) throws NotFoundException;
    Collection<Transaction> saveMultiple(Collection<Transaction> transactions);
    Collection<Transaction> findUserLendings(final Long userId);
    Collection<Transaction> findUserBorrowings(final Long userId);
    Collection<Transaction> findTransactionsWithinGroup(final Long associatedGroupId);
    Transaction softDeleteById(final Long id) throws NotFoundException;
}
