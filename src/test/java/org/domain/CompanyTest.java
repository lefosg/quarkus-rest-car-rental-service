package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.Currency;
import org.util.Money;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {
    //todo: test getters & setters -> VAS

    Company company;

    @BeforeEach
    public void setup() {
        company = new Company("etaireia1","123456789", "123456798",
                "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb",null);
    }


    @Test
    public void equalsSameCompany() {
        assertEquals(company, company);
    }

    @Test
    public void equalsDifferentCompany() {
        Company company2 = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb",null);
        assertEquals(company, company2);
    }

    @Test
    public void notEqualsDifferentCompany() {
        Company company2 = new Company("etaireia2","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb",null);
        assertNotEquals(company, company2);
    }

    @Test
    public void testGetAFM() {
        assertEquals("GObbb", company.getAFM());

    }

    @Test
    public void testSetAFM() {
        company.setAFM("123");
        assertEquals("123", company.getAFM());
    }

    @Test
    public void testGetIBAN() {
        assertEquals("GObbb", company.getIBAN());
    }

    @Test
    public void testSetIBAN() {
        company.setIBAN("peiraivs");
        assertEquals("peiraivs", company.getIBAN());
    }

    @Test
    public void testSetMoney(){
        Money money = new Money(10, Currency.USD);
        assertNotEquals(money, new Money());
    }

    @Test
    public void testnotEqualsMoneyCurrency() {
        Money money = new Money(10, Currency.USD);
        assertNotEquals(company, money);
    }

    @Test
    public void testGetIncome() {
        Money money = new Money(0);
        assertEquals(money, company.getIncome());
    }

    @Test
    public void testSetIncome() {
    Money money = new Money(0);
    assertEquals(money, company.getIncome());
    }

    @Test
    public void testgGetDamage_cost() {
        Money damage_cost = new Money(0);
        assertEquals(damage_cost, company.getDamage_cost());
    }

    @Test
    public void testSetDamage_cost() {
        Money damage_cost = new Money(0);
    assertEquals(damage_cost, company.getDamage_cost());}
}