package app.clip.commons;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Money implements Serializable {
    private BigDecimal amount;
    private Currency currency;

    public Money() {
        this.amount = BigDecimal.ZERO;
    }

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && currency == money.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currency=" + currency +
                '}';
    }


    public static Money negate(Money a) {
        if (a.getAmount() != null) {
            BigDecimal newAmount = a.getAmount().negate();
            return new Money(newAmount, a.getCurrency());
        }
        return new Money(a.getAmount(), a.getCurrency());
    }

    public static Money add(Money a, Money b) {
        if (a.getCurrency() != b.getCurrency()) throw new IllegalArgumentException("Cannot sum two different currencies!");
        BigDecimal total = a.getAmount().add(b.getAmount());
        return new Money(total, a.getCurrency());
    }

    public static Money subtract(Money a, Money b) {
        if (a.getCurrency() != b.getCurrency()) throw new IllegalArgumentException("Cannot subtract two different currencies!");
        BigDecimal total = a.getAmount().subtract(b.getAmount());
        return new Money(total, a.getCurrency());
    }

    public static Money minimum(Money a, Money b) {
        if (a.getCurrency() != b.getCurrency()) throw new IllegalArgumentException("Cannot find minimum between two different currencies!");
        if (a.getAmount().compareTo(b.getAmount()) < 0) return a;
        else return b;
    }

    public static Money abs(Money a) {
        if (a == null) return null;
        return new Money(a.getAmount().abs(), a.getCurrency());
    }

}
