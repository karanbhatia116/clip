package app.clip.transactions.utils;


import app.clip.commons.money.Money;
import app.clip.transactions.models.Transaction;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionUtils {

    public static List<Transaction> simplifyTransactions(final Collection<Transaction> transactions) {
        final Map<Long, Money> netUserBalances = calculateNetUserBalances(transactions);
        final Map<Long, Money> lenders = getLenders(netUserBalances);
        final Map<Long, Money> borrowers = getBorrowers(netUserBalances);
        final PriorityQueue<PersonBalance> lendersQueue = getLendersQueue(lenders);
        final PriorityQueue<PersonBalance> borrowersQueue = getBorrowersQueue(borrowers);
        final List<Transaction> simplifiedTransactions = new ArrayList<>();
        while (!lendersQueue.isEmpty() && !borrowersQueue.isEmpty()) {
            final PersonBalance lender = lendersQueue.poll();
            final PersonBalance borrower = borrowersQueue.poll();

            if (lender == null || borrower == null)
                break;

            Money minimumAmount = Money.minimum(lender.getBalance(), Money.abs(borrower.getBalance()));
            lender.setBalance(Money.subtract(lender.getBalance(), minimumAmount));
            borrower.setBalance(Money.add(borrower.getBalance(), minimumAmount));

            if (lender.getBalance().getAmount().compareTo(BigDecimal.ZERO) == 0 ||
                    borrower.getBalance().getAmount().compareTo(BigDecimal.ZERO) == 0 ) {
                Transaction newTransaction = new Transaction();
                newTransaction.setActive(true);
                newTransaction.setAmt(minimumAmount);
                newTransaction.setLenderId(lender.getPersonId());
                newTransaction.setBorrowerId(borrower.getPersonId());
                newTransaction.setAssociatedGroupId(transactions.iterator().next().getAssociatedGroupId());
                // TODO: fetch other details from a map
                simplifiedTransactions.add(newTransaction);
            }

            if (lender.getBalance().getAmount().compareTo(BigDecimal.ZERO) > 0) {
                lendersQueue.offer(lender);
            }

            if (borrower.getBalance().getAmount().compareTo(BigDecimal.ZERO) < 0) {
                borrowersQueue.offer(borrower);
            }
        }

        if (lendersQueue.isEmpty() && borrowersQueue.isEmpty()) return simplifiedTransactions;

        throw new IllegalStateException("Total of lenders and borrowers in transactions do not match!!!");
    }

    private static Map<Long, Money> calculateNetUserBalances (final Collection<Transaction> transactions) {

        final Map<Long, Money> netUserBalances = new HashMap<>();
        if (transactions == null || transactions.isEmpty()) return netUserBalances;

        transactions.forEach(transaction -> {
            Long lenderId = transaction.getLenderId();
            Long borrowerId = transaction.getBorrowerId();
            addToBalance(netUserBalances, lenderId, transaction.getAmt());
            subtractFromBalance(netUserBalances, borrowerId, transaction.getAmt());
        });
        return netUserBalances;
    }

    // should only mutate the net user balances map
    private static void addToBalance(Map<Long, Money> netUserBalances, final Long userId, final Money transactionAmount) {
        if (netUserBalances.containsKey(userId)) {
            final Money currentBalance = netUserBalances.get(userId);
            final Money newBalance = Money.add(currentBalance, transactionAmount);
            netUserBalances.put(userId, newBalance);
        }
        else {
            netUserBalances.put(userId, transactionAmount);
        }
    }


    // should only mutate the net user balances map
    private static void subtractFromBalance(Map<Long, Money> netUserBalances, final Long userId, final Money transactionAmount) {
        if (netUserBalances.containsKey(userId)) {
            final Money currentBalance = netUserBalances.get(userId);
            final Money newBalance = Money.subtract(currentBalance, transactionAmount);
            netUserBalances.put(userId, newBalance);
        }
        else {
            Money newAmount = new Money(transactionAmount.getAmount().negate(), transactionAmount.getCurrency());
            netUserBalances.put(userId, newAmount);
        }
    }

    private static Map<Long, Money> getLenders(final Map<Long, Money> netUserBalances) {
        return netUserBalances.entrySet().stream().filter(it -> it.getValue().getAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));
    }

    private static Map<Long, Money> getBorrowers(final Map<Long, Money> netUserBalances) {
        return netUserBalances.entrySet().stream().filter(it -> it.getValue().getAmount().compareTo(BigDecimal.ZERO) < 0).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));
    }

    private static PriorityQueue<PersonBalance> getLendersQueue(final Map<Long, Money> lenders) {
        PriorityQueue<PersonBalance> lendersQueue = new PriorityQueue<>(personBalanceComparator().reversed());
        lenders.forEach((key, value) -> {
            PersonBalance personBalance = new PersonBalance(key, value);
            lendersQueue.offer(personBalance);
        });
        return lendersQueue;
    }

    private static PriorityQueue<PersonBalance> getBorrowersQueue(final Map<Long, Money> borrowers) {
        PriorityQueue<PersonBalance> borrowersQueue = new PriorityQueue<>(personBalanceComparator());
        borrowers.forEach((key, value) -> {
            PersonBalance personBalance = new PersonBalance(key, value);
            borrowersQueue.offer(personBalance);
        });
        return borrowersQueue;
    }

    static class PersonBalance {
        private Long personId;
        private Money balance;

        public PersonBalance(Long personId, Money balance) {
            this.personId = personId;
            this.balance = balance;
        }

        public PersonBalance() {
        }

        public Long getPersonId() {
            return personId;
        }

        public void setPersonId(Long personId) {
            this.personId = personId;
        }

        public Money getBalance() {
            return balance;
        }

        public void setBalance(Money balance) {
            this.balance = balance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PersonBalance that = (PersonBalance) o;
            return Objects.equals(personId, that.personId) && Objects.equals(balance, that.balance);
        }

        @Override
        public int hashCode() {
            return Objects.hash(personId, balance);
        }

        @Override
        public String toString() {
            return "PersonBalance{" +
                    "personId=" + personId +
                    ", balance=" + balance +
                    '}';
        }
    }

    private static Comparator<PersonBalance> personBalanceComparator() {
        return new Comparator<PersonBalance>() {
            @Override
            public int compare(PersonBalance a, PersonBalance b) {
                if (a == null || b == null) return 0;
                return a.balance.getAmount().compareTo(b.balance.getAmount());
            }
        };
    }
}