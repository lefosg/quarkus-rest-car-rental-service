package org.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.util.Currency.EUR;
import static org.util.Currency.USD;

public class MoneyTest {

    Money money;
    Money money1;
    Money money2;
    Money money3;
    @BeforeEach
    public void setUp(){
        money = new Money(55);
        money1 = new Money(45);
        money2 = new Money(55,USD);
        money3= new Money(55);
    }
    @Test
    public void equalsSameMoney(){
        assertEquals(money,money);
    }

    @Test
    public void equalsSameMoneyWithAllTheSame(){
        assertEquals(money.getAmount(),money3.getAmount());
    }

    @Test
    public void notEqualsMoneyAmount(){
        assertNotEquals(money.getAmount(),money1.getAmount());
    }

    @Test
    public void moneyEqualsEUR(){
        assertEquals(money.getCurrency(),EUR);
    }
    @Test
    public void notEqualsMoneyWithSameAmount(){
        assertNotEquals(money.getCurrency(),money2.getCurrency());
    }

    @Test
    public void setNewAmount(){
        money.setAmount(45);
        assertEquals(money.getAmount(),money1.getAmount());
    }

    @Test
    public void setNewCurrency(){
        money.setCurrency(USD);
        assertEquals(money.getCurrency(),money2.getCurrency());
    }

}
