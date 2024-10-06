package app.clip.expenses.api;

import app.clip.commons.exceptions.NotFoundException;
import app.clip.commons.exceptions.ValidationException;
import app.clip.commons.money.Currency;
import app.clip.commons.money.Money;
import app.clip.commons.exceptions.Violation;
import app.clip.expenses.adapters.ExpenseDTOAdapter;
import app.clip.expenses.dtos.ExpenseDTO;
import app.clip.expenses.models.Expense;
import app.clip.expenses.services.ExpenseService;
import app.clip.expenses.validators.ExpenseDTOValidator;
import app.clip.transactions.models.Transaction;
import app.clip.transactions.services.TransactionService;
import app.clip.groups.models.Group;
import app.clip.groups.services.GroupService;
import app.clip.splits.models.Split;
import app.clip.splits.services.SplitService;
import app.clip.transactions.utils.TransactionUtils;
import app.clip.users.models.User;
import app.clip.users.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/expenses")
public class ExpenseAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseAPI.class);
    private final UserService userService;
    private final GroupService groupService;
    private final ExpenseService expenseService;
    private final SplitService splitService;
    private final TransactionService transactionService;

    public ExpenseAPI(UserService userService, GroupService groupService, ExpenseService expenseService, SplitService splitService, TransactionService transactionService) {
        this.userService = userService;
        this.groupService = groupService;
        this.expenseService = expenseService;
        this.splitService = splitService;
        this.transactionService = transactionService;
    }


    @PostMapping("/")
    public ExpenseDTO addExpense(@RequestBody ExpenseDTO expenseDTO) throws ValidationException, NotFoundException {
        final List<Violation> violations = ExpenseDTOValidator.validateExpenseDTO(expenseDTO);
        if (!violations.isEmpty()) {
            LOGGER.error("Validation failed for expense request: {}", violations);
            throw new ValidationException(violations);
        }

        final Expense expense = ExpenseDTOAdapter.toExpense(expenseDTO);
        final User creator = userService.getById(expense.getCreatorId());
        final User payer = userService.getById(expense.getPayerId());
        final Group associatedGroup;
        if (expense.getAssociatedGroupId() != null) {
            associatedGroup = groupService.getById(expense.getAssociatedGroupId());
        } else {
            associatedGroup = null;
        }

        final List<Split> splits = expenseDTO.splits();

        // persist expense
        final Expense addedExpense = expenseService.add(expense);
        splits.forEach(split -> split.setExpenseId(addedExpense.getId()));

        // persist splits
        Collection<Split> savedSplits = splitService.saveMultiple(splits);

        // register transactions
        final List<Transaction> transactions = new ArrayList<>();
        savedSplits.forEach(split -> {
            if (!Objects.equals(split.getUserId(), payer.getId())) {
                Transaction transaction = new Transaction();
                transaction.setLenderId(payer.getId());
                transaction.setBorrowerId(split.getUserId());
                Money money = new Money(
                        split.getSplitAmount().getAmount().abs(),
                        split.getSplitAmount().getCurrency()
                );

                transaction.setAmt(money);
                if (associatedGroup != null) {
                    transaction.setAssociatedGroupId(associatedGroup.getId());
                }
                transactions.add(transaction);
            }
        });
        if(associatedGroup != null && associatedGroup.getSimplifyDebt() == Boolean.TRUE) {
            Collection<Transaction> transactionsWithinGroup = transactionService.findTransactionsWithinGroup(associatedGroup.getId());
            transactions.addAll(transactionsWithinGroup);
            Collection<Transaction> simplifiedTransactions = TransactionUtils.simplifyTransactions(transactions);
            transactionService.saveMultiple(simplifiedTransactions);
            return expenseDTO;
        }
        transactionService.saveMultiple(transactions);
        return expenseDTO;
    }


    // TODO: add support for pagination here
    // TODO: also add support for quering here instead of if else.
    @GetMapping("/")
    public List<Expense> fetchExpenses(@RequestParam(value = "associatedGroupId", required = false) Long groupId,
                                       @RequestParam(value = "userId", required = false) Long userId
    ) {
        if (groupId != null) {
            return expenseService.fetchAllInGroup(groupId);
        }
        else if (userId != null) {
            return expenseService.fetchWhereUserInvolved(userId);
        }
        else {
            return expenseService.fetchAll();
        }
    }

    @GetMapping("/{id}")
    public ExpenseDTO viewExpense(@PathVariable("id") Long id) throws NotFoundException {
        Expense expense = expenseService.getById(id);
        Collection<Split> splits = splitService.fetchAllSplitsForExpense(id);
        return ExpenseDTOAdapter.toExpenseDTO(expense, splits);
    }


    @GetMapping("/total-spendings/{groupId}")
    public List<Money> totalGroupSpendings(@PathVariable("groupId") Long groupId) {
        List<Expense> expensesInGroup = expenseService.fetchAllInGroup(groupId);
        Map<Currency, Money> totalSpendingAmountPerCurrency = new HashMap<>();
        for (Expense expense : expensesInGroup) {
            Currency currency = expense.getTotalAmount().getCurrency();
            if (totalSpendingAmountPerCurrency.containsKey(currency)) {
                Money existingTotal = totalSpendingAmountPerCurrency.get(currency);
                BigDecimal newTotal = existingTotal.getAmount().add(expense.getTotalAmount().getAmount());
                existingTotal.setAmount(newTotal);
                totalSpendingAmountPerCurrency.put(currency, existingTotal);
            }
            else {
                totalSpendingAmountPerCurrency.put(currency, expense.getTotalAmount());
            }
        }

        return totalSpendingAmountPerCurrency.values().stream().toList();
    }
}
