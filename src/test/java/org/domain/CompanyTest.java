package org.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.Currency;
import org.util.DamageType;
import org.util.Money;
import org.util.VehicleType;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    Company company;

    @BeforeEach
    public void setup() {
        company = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");

        LinkedHashMap<Integer, Float> mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(100, 0.10f);
        mileage_scale.put(200, 0.20f);
        mileage_scale.put(300, 0.30f);

        LinkedHashMap<DamageType, Float> damage_type = new LinkedHashMap<DamageType, Float>();
        damage_type.put(DamageType.Glasses,50f);
        damage_type.put(DamageType.Machine,200f);
        damage_type.put(DamageType.Tyres,100f);
        damage_type.put(DamageType.Scratches,15f);
        damage_type.put(DamageType.Interior,40f);

        ChargingPolicy policy = new ChargingPolicy(mileage_scale,damage_type);

        company.setPolicy(policy);
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

    @Test
    public void calculateMileageCostNegativeMiles() {
        assertThrows(IllegalArgumentException.class, () -> {
            company.calculateMileageCost(-1);
        });
    }

    @Test
    public void calculateMileageCostZeroMiles() {
        Money cost = company.calculateMileageCost(0);
        assertEquals(new Money(0), cost);
    }

    @Test
    public void calculateMileageCostScale1() {
        Money cost = company.calculateMileageCost(50);
        assertEquals(new Money(5), cost);
        //boundary case
        cost = company.calculateMileageCost(100);
        assertEquals(new Money(10), cost);
    }

    @Test
    public void calculateMileageCostScale2() {
        Money cost = company.calculateMileageCost(150);
        assertEquals(new Money(20), cost);
        //boundary case
        cost = company.calculateMileageCost(200);
        assertEquals(new Money(30), cost);
    }

    @Test
    public void calculateMileageCostScale3() {
        Money cost = company.calculateMileageCost(250);
        assertEquals(new Money(45), cost);
        //boundary case
        cost = company.calculateMileageCost(300);
        assertEquals(new Money(60), cost);
    }

    @Test
    public void calculateMileageCostLastScale() {
        //fixme last scale
        //over last scale case
        Money cost = company.calculateMileageCost(350);
        assertEquals(new Money(400), cost);
    }

    @Test
    public void calculateDamageCostNullDamageType() {
        assertThrows(NullPointerException.class, () -> {
            company.calculateDamageCost(VehicleType.Hatchback, null);
        });
    }

    @Test
    public void calculateDamageCostNoDamageType() {
        assertThrows(NullPointerException.class, () -> {
            company.calculateDamageCost(VehicleType.Hatchback, DamageType.NoDamage);
        });
    }

    @Test
    public void calculateDamageCostAllDamageTypes() {
        Money cost = company.calculateDamageCost(VehicleType.Hatchback, DamageType.Glasses);
        assertEquals(cost, new Money(50));

        cost = company.calculateDamageCost(VehicleType.Hatchback, DamageType.Machine);
        assertEquals(cost, new Money(200));

        cost = company.calculateDamageCost(VehicleType.Hatchback, DamageType.Tyres);
        assertEquals(cost, new Money(100));

        cost = company.calculateDamageCost(VehicleType.Hatchback, DamageType.Scratches);
        assertEquals(cost, new Money(15));

        cost = company.calculateDamageCost(VehicleType.Hatchback, DamageType.Interior);
        assertEquals(cost, new Money(40));
    }

    @Test
    public void calculateFixedCharge() {

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