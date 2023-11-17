package org.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {
    //todo: test getters & setters -> VAS

    Company company;

    @BeforeEach
    public void setup() {
        HashMap<Integer, Float> hash1=new HashMap<>();

        HashMap<VehicleType, Integer> hash2= new HashMap<>();
        hash1.put(55, 2.5F);
        hash1.put(100,3.5F);

        hash2.put(VehicleType.SUV,5);
        company = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb",hash1,hash2);
    }
    @Test
    public void checkHashTables(){
        HashMap<Integer, Float> hash3=new HashMap<>();
        hash3.put(55, 2.5F);
        hash3.put(100,3.5F);
        HashMap test1=company.getMileageScale();
        assertEquals(test1,hash3);
    }
    @Test
    public void equalsSameCompany() {
        assertEquals(company, company);
    }

    @Test
    public void equalsDifferentCompany() {
        HashMap<Integer, Float> hash1=new HashMap<>();
        HashMap<VehicleType, Integer> hash2= new HashMap<>();
        hash1.put(55, 2.5F);
        hash2.put(VehicleType.SUV,5);
        Company company2 = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb",hash1,hash2);
        assertEquals(company, company2);
    }

    @Test
    public void notEqualsDifferentCompany() {
        HashMap<Integer, Float> hash1=new HashMap<>();
        HashMap<VehicleType, Integer> hash2= new HashMap<>();
        hash1.put(55, 2.5F);
        hash2.put(VehicleType.SUV,5);
        Company company2 = new Company("etaireia2","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb",hash1,hash2);
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