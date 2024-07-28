package app.clip.transactions.utils;

import app.clip.commons.Currency;
import app.clip.commons.Money;
import app.clip.transactions.models.Transaction;

import java.math.BigDecimal;
import java.util.*;

public class TransactionUtils {

    // TODO: fix this code!!!!
    public static Collection<Transaction> simplifyTransactions(Collection<Transaction> transactions) {
        Map<Long, Map<Currency, BigDecimal>> balances = new HashMap<>();

        // Step 1: Calculate net balances
        for (Transaction t : transactions) {
            updateBalance(balances, t.getLenderId(), t.getAmt().getCurrency(), t.getAmt().getAmount());
            updateBalance(balances, t.getBorrowerId(), t.getAmt().getCurrency(), t.getAmt().getAmount().negate());
        }

        // Step 2: Create simplified transactions
        List<Transaction> simplifiedTransactions = new ArrayList<>();

        for (Map.Entry<Long, Map<Currency, BigDecimal>> entry : balances.entrySet()) {
            Long userId = entry.getKey();
            for (Map.Entry<Currency, BigDecimal> balanceEntry : entry.getValue().entrySet()) {
                Currency currency = balanceEntry.getKey();
                BigDecimal amount = balanceEntry.getValue();

                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    // This user owes money
                    for (Map.Entry<Long, Map<Currency, BigDecimal>> creditorEntry : balances.entrySet()) {
                        Long creditorId = creditorEntry.getKey();
                        BigDecimal creditorBalance = creditorEntry.getValue().getOrDefault(currency, BigDecimal.ZERO);

                        if (creditorBalance.compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal transferAmount = amount.abs().min(creditorBalance);

                            Transaction newTransaction = new Transaction();
                            newTransaction.setLenderId(creditorId);
                            newTransaction.setBorrowerId(userId);
                            newTransaction.setAmt(new Money(transferAmount, currency));
                            newTransaction.setAssociatedGroupId(transactions.iterator().next().getAssociatedGroupId());
                            newTransaction.setActive(true);
                            simplifiedTransactions.add(newTransaction);

                            // Update balances
                            amount = amount.add(transferAmount);
                            updateBalance(balances, userId, currency, transferAmount);
                            updateBalance(balances, creditorId, currency, transferAmount.negate());

                            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        return simplifiedTransactions;
    }

    private static void updateBalance(Map<Long, Map<Currency, BigDecimal>> balances, Long userId, Currency currency, BigDecimal amount) {
        balances.computeIfAbsent(userId, k -> new HashMap<>())
                .merge(currency, amount, BigDecimal::add);
    }
}
