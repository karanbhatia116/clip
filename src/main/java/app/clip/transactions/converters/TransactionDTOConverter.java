package app.clip.transactions.converters;

import app.clip.transactions.dtos.TransactionDTO;
import app.clip.transactions.models.Transaction;

public class TransactionDTOConverter {

    public static Transaction toTransaction(TransactionDTO transactionDTO) {
        if (transactionDTO == null) return null;

        Transaction transaction = new Transaction();
        transaction.setLenderId(transactionDTO.userId());
        transaction.setBorrowerId(transactionDTO.friendId());
        transaction.setAmt(transactionDTO.splitAmount());
        transaction.setAssociatedGroupId(transactionDTO.associatedGroupId());
        if (transactionDTO.id() != null) {
            transaction.setId(transactionDTO.id());
        }

        return transaction;
    }

}
