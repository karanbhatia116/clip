package app.clip.transactions.api;

import app.clip.commons.Currency;
import app.clip.commons.Money;
import app.clip.transactions.models.Transaction;
import app.clip.transactions.services.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/transactions")
public class TransactionsAPI {

    private final TransactionService transactionService;

    public TransactionsAPI(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/total-floating-amount/{userId}")
    public List<Money> getTotalFloatingAmountForUser(@PathVariable("userId") Long userId) {
        Collection<Transaction> lendings = transactionService.findUserLendings(userId);
        Collection<Transaction> borrowings = transactionService.findUserBorrowings(userId);
        Logger.getAnonymousLogger().log(Level.INFO, "Lendings: " + lendings);
        Logger.getAnonymousLogger().log(Level.INFO, "Borrowings: " + borrowings);
        Map<Currency, Money> splitAmountsByCurrency = new HashMap<>();
        lendings.forEach(lending -> {
            Currency currency = lending.getAmt().getCurrency();
            if (splitAmountsByCurrency.containsKey(currency)) {
                Money existingTotalForCurrency = splitAmountsByCurrency.get(currency);
                BigDecimal newTotal = existingTotalForCurrency.getAmount().add(lending.getAmt().getAmount());
                existingTotalForCurrency.setAmount(newTotal);
                splitAmountsByCurrency.put(currency, existingTotalForCurrency);
            }
            else {
                splitAmountsByCurrency.put(currency, lending.getAmt());
            }
        });

        borrowings.forEach(borrowing -> {
            Currency currency = borrowing.getAmt().getCurrency();
            if (splitAmountsByCurrency.containsKey(currency)) {
                Money existingTotalForCurrency = splitAmountsByCurrency.get(currency);
                BigDecimal newTotal = existingTotalForCurrency.getAmount().add(borrowing.getAmt().getAmount().abs().multiply(BigDecimal.valueOf(-1)));
                existingTotalForCurrency.setAmount(newTotal);
                splitAmountsByCurrency.put(currency, existingTotalForCurrency);
            }
            else {
                BigDecimal borrowedAmount = borrowing.getAmt().getAmount().abs();
                BigDecimal negativeBorrowedAmount = borrowedAmount.multiply(BigDecimal.valueOf(-1));
                Money borrowedMoney = new Money(negativeBorrowedAmount, currency);
                splitAmountsByCurrency.put(currency, borrowedMoney);
            }
        });

        return splitAmountsByCurrency.values().stream().toList();
    }

    @PostMapping("/settle")
    public Transaction settleFriendship (@RequestBody Transaction transaction) {
        return transactionService.save(transaction);
    }

    @GetMapping("/{groupId}")
    public List<Transaction> getTransactionsInGroup(@PathVariable("groupId") Long groupId) {
        return transactionService.findTransactionsWithinGroup(groupId).stream().toList();
    }
}
