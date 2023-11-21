package org.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.Currency;
import org.util.DamageType;
import org.util.Money;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    Company company;

    @BeforeEach
    public void setup() {
        company = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
    }

    @Test
    public void equalsSameCompany() {
        assertEquals(company, company);
    }

    @Test
    public void equalsDifferentCompany() {
        Company company2 = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
        assertEquals(company, company2);
    }

    @Test
    public void notEqualsDifferentCompany() {
        Company company2 = new Company("SPEED","speed@gmail.com", "ilovecookies",
                "2644125415","ΛΕΥΚΩΣΙΑΣ 66","ΠΑΤΡΑ","34785","999641227","GR3687254378963625");
        assertNotEquals(company, company2);
    }

    @Test
    public void checkDifferentPolicies(){
        Company company1 = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
        Company company2 = new Company("SPEED","speed@gmail.com", "ilovecookies",
                "2644125415","ΛΕΥΚΩΣΙΑΣ 66","ΠΑΤΡΑ","34785","999641227","GR3687254378963625");

        LinkedHashMap<Integer, Float> mileage_scale1 = new LinkedHashMap<Integer, Float>();
        mileage_scale1.put(150, 0.10f);
        mileage_scale1.put(200, 0.20f);
        mileage_scale1.put(300, 0.30f);

        LinkedHashMap<DamageType, Float> damage_type1 = new LinkedHashMap<DamageType, Float>();
        damage_type1.put(DamageType.Glasses,50f);
        damage_type1.put(DamageType.Machine,30f);
        ChargingPolicy policy1 = new ChargingPolicy(mileage_scale1,damage_type1);
        company1.setPolicy(policy1);

        LinkedHashMap<Integer, Float> mileage_scale2 = new LinkedHashMap<Integer, Float>();
        mileage_scale2.put(150, 0.10f);
        mileage_scale2.put(200, 0.20f);
        mileage_scale2.put(300, 0.30f);

        LinkedHashMap<DamageType, Float> damage_type2 = new LinkedHashMap<DamageType, Float>();
        damage_type2.put(DamageType.Tyres,50f);
        damage_type2.put(DamageType.Machine,50f);
        ChargingPolicy policy2 = new ChargingPolicy(mileage_scale2,damage_type2);
        company2.setPolicy(policy2);
        assertNotEquals(company1.getPolicy(),company2.getPolicy());

    }




    // getters & setters

    @Test
    public void testGetAFM() {
        assertEquals("163498317", company.getAFM());
    }

    @Test
    public void testSetAFM() {
        company.setAFM("123");
        assertEquals("123", company.getAFM());
    }

    @Test
    public void testGetIBAN() {
        assertEquals("GR2514526358789654", company.getIBAN());
    }

    @Test
    public void testSetIBAN() {
        company.setIBAN("peiraivs");
        assertEquals("peiraivs", company.getIBAN());
    }

    @Test
    public void testIncomeGetMoneyEUR(){
        Money money = new Money(0);
        assertEquals(money, company.getIncome());
    }

    @Test
    public void testIncomeGetMoneyUSD(){
        Money money = new Money(0, Currency.USD);
        assertNotEquals(money, company.getIncome());
    }

    @Test
    public void testIncomeSetMoney() {
        Money money = new Money(10, Currency.USD);
        company.setIncome(money);
        assertEquals(money, company.getIncome());
    }

    @Test
    public void testDamageCostGetMoneyEUR(){
        Money money = new Money(0);
        assertEquals(money, company.getDamage_cost());
    }

    @Test
    public void testDamageCostGetMoneyUSD(){
        Money money = new Money(0, Currency.USD);
        assertNotEquals(money, company.getDamage_cost());
    }

    @Test
    public void testDamageCostSetMoney() {
        Money money = new Money(10, Currency.USD);
        company.setDamage_cost(money);
        assertEquals(money, company.getDamage_cost());
    }
}