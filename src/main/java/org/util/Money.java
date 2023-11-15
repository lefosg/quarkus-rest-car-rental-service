package org.util;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;


@Embeddable
public class Money {

    public enum Currency {EUR, USD}

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    public Money() {
    }

    public Money(int amount) {
        this.amount = amount;
        this.currency = Money.Currency.EUR;
    }

    public Money(int amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currency=" + currency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return getAmount() == money.getAmount() && getCurrency() == money.getCurrency();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount(), getCurrency());
    }
}
