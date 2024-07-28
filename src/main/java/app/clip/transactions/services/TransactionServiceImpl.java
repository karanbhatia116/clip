package app.clip.transactions.services;

import app.clip.transactions.models.Transaction;
import app.clip.transactions.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getById(Long id) {
        return transactionRepository.findById(id).filter(Transaction::isActive).orElseThrow(() -> new NoSuchElementException("No transaction found with id " + id));
    }

    /**
     * Hard deletes the transaction
     */
    @Override
    public Transaction deleteById(Long id) {
        Transaction transaction = getById(id);
        transactionRepository.deleteById(id);
        return transaction;
    }



    @Override
    public Collection<Transaction> saveMultiple(Collection<Transaction> transactions) {
        transactions.forEach(transaction -> {
            transaction.setPaidOn(System.currentTimeMillis());
            transaction.setActive(true);
        });
        return transactions.stream().map(this::save).toList();
    }

    @Override
    public Collection<Transaction> findUserLendings(final Long userId) {
        return transactionRepository.findByLenderId(userId).stream().filter(Transaction::isActive).toList();
    }

    @Override
    public Collection<Transaction> findUserBorrowings(final Long userId) {
        return transactionRepository.findByBorrowerId(userId).stream().filter(Transaction::isActive).toList();
    }

    @Override
    public Collection<Transaction> findTransactionsWithinGroup(Long associatedGroupId) {
        return transactionRepository.findByAssociatedGroupId(associatedGroupId).stream().filter(Transaction::isActive).toList();
    }

    /**
     * Marks transaction as inactive.
     */
    @Override
    public Transaction softDeleteById(Long id) {
        transactionRepository.markAsInactive(id);
        return transactionRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No transaction found with id " + id));
    }

}
