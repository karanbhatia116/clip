package app.clip.converters;

import app.clip.commons.money.Currency;
import app.clip.commons.money.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyAttributeConverter implements AttributeConverter<Money, String> {

    @Override
    public String convertToDatabaseColumn(Money money) {
        if (money == null) return null;
        return money.getCurrency().name() + " " + money.getAmount();
    }

    @Override
    public Money convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return null;
        String[] parts = dbData.split(" ");
        BigDecimal amount = new BigDecimal(parts[1]);
        Currency currency = Currency.getInstance(parts[0]);
        return new Money(amount, currency);
    }
}
