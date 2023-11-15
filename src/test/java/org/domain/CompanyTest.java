package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {
    //todo: test getters & setters
    Company company;

    @BeforeEach
    public void setup() {
        company = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");;
    }

    @Test
    public void equalsSameCompany() {
        assertEquals(company, company);
    }

    @Test
    public void equalsDifferentCompany() {
        Company company2 = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");;
        assertEquals(company, company2);
    }

    @Test
    public void notEqualsDifferentCompany() {
        Company company2 = new Company("etaireia2","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");;
        assertNotEquals(company, company2);
    }

    @Test
    void getAFM() {
    }

    @Test
    void setAFM() {
    }

    @Test
    void getIBAN() {
    }

    @Test
    void setIBAN() {
    }

    @Test
    void getIncome() {
    }

    @Test
    void setIncome() {
    }

    @Test
    void getDamage_cost() {
    }

    @Test
    void setDamage_cost() {
    }
}