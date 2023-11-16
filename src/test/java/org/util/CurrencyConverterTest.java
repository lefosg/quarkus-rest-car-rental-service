package org.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.util.Currency.EUR;
import static org.util.Currency.USD;

class CurrencyConverterTest {

    CurrencyConverter cnv;
    Money moneyEUR, moneyUSD;

    @BeforeEach
    public void setup() {
        cnv = new CurrencyConverter();
        moneyEUR = new Money(55, EUR);
        moneyUSD = new Money(55, USD);
    }

    @Test
    public void currencyConverterNotNull() {
        assertNotNull(cnv);
    }

    @Test
    public void converterEurToUsd(){
        cnv.currencyConverter(moneyEUR);
        assertEquals(moneyEUR.getCurrency(),USD);
    }

    @Test
    public void converterUsdToEur(){
        cnv.currencyConverter(moneyUSD);
        assertEquals(moneyUSD.getCurrency(),EUR);
    }
}