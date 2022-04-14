package io.github.mityavasilyev.interviewunlimintparser.model;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Getter
public enum Currency {
    USD("USD", "United States Dollar", '$'),
    GBP("GBP", "Great Britain Pound", '£'),
    EUR("EUR", "Euro", '€'),
    JPY("JPY", "Japan Yen"),
    RUB("RUB", "Russian Ruble", '₽'),
    UNKNOWN("UNKNOWN", "Unknown Currency", '?');

    private final String shortName;
    private final String fullName;
    private final char symbol;

    Currency(String shortName, String fullName, char symbol) {
        this.shortName = shortName.toUpperCase(Locale.ROOT);
        this.fullName = StringUtils.capitalize(fullName);
        this.symbol = symbol;
    }

    Currency(String shortName, String fullName) {
        this(shortName, fullName, '?');
    }
}
