package app.clip.transactions.services;

import app.clip.commons.constants.AssetClass;
import app.clip.commons.exceptions.NotFoundException;
import app.clip.transactions.models.Transaction;
import app.clip.transactions.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getById(Long id) throws NotFoundException {
        return transactionRepository.findById(id).filter(Transaction::isActive).orElseThrow(() -> new NotFoundException(AssetClass.TRANSACTION.name(), id.toString()));
    }

    /**
     * Hard deletes the transaction
     */
    @Override
    @Transactional
    public Transaction deleteById(Long id) throws NotFoundException {
        Transaction transaction = getById(id);
        transactionRepository.deleteById(id);
        return transaction;
    }



    @Override
    @Transactional
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
    @Transactional
    public Transaction softDeleteById(Long id) throws NotFoundException {
        transactionRepository.markAsInactive(id);
        return transactionRepository.findById(id).orElseThrow(() -> new NotFoundException(AssetClass.TRANSACTION.name(), id.toString()));
    }

}
