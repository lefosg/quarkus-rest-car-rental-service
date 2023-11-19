package org.persistence;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import org.domain.ChargingPolicy;
import org.domain.Company;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.util.DamageType;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.List;

class CompanyJPATest extends JPATest{

    @Test
    public void listCompanies() {
        List<Company> companies = em.createQuery("select c from Company c").getResultList();
        assertEquals(2, companies.size());
    }

    @Test
    public void denySavingCompanyWithSameName() {

        Company company1 = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
        Company company2 = new Company("etaireia2","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(company1);
            em.persist(company2);
            tx.commit();
        });
    }

    @Test
    public void checkDifferentPolicies(){
        Company company1 = new Company("etaireia1","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
        Company company2 = new Company("etaireia2","123456789", "123456798", "GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");

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


}