package org.util;

import static org.util.Money.Currency.EUR;
import static org.util.Money.Currency.USD;


public class CurrencyConverter {

    private static final double eur_to_usd = 1.10;
    private static final double usd_to_eur = 0.90;

    public void currencyConverter(Money money) {

        if (money.getCurrency() == USD) {
            money.setAmount(money.getAmount()*usd_to_eur);
            money.setCurrency(EUR);
        };
        if (money.getCurrency() == EUR) {
            money.setAmount(money.getAmount()*eur_to_usd);
            money.setCurrency(USD);
        }

    }
}
