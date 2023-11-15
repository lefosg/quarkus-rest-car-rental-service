package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {
    //todo: test getters & setters -> VAS

    Company company;

    @BeforeEach
    public void setup() {
        company = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
    }

    @Test
    public void equalsSameCompany() {
        assertEquals(company, company);
    }

    @Test
    public void equalsDifferentCompany() {
        Company company2 = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
        assertEquals(company, company2);
    }

    @Test
    public void notEqualsDifferentCompany() {
        Company company2 = new Company("etaireia2","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
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
    public void testGetIncome() {
    }

    @Test
    public void testSetIncome() {
    }

    @Test
    public void testgGetDamage_cost() {
    }

    @Test
    public void testSetDamage_cost() {
    }
}