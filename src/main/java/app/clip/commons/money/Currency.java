package app.clip.commons.money;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Currency {
    INR,
    USD,
    EUR,
    AUD,
    UNKNOWN;

    private static final Logger LOGGER = LoggerFactory.getLogger(Currency.class);

    public static Currency getInstance(String value) {
        try {
            return Currency.valueOf(value);
        }
        catch (Exception e) {
            LOGGER.error("Unknown currency: {}", value);
            return Currency.UNKNOWN;
        }
    }

}
