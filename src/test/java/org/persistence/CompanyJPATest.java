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
    public void denySavingCompanyWithSameAFM() {
        Company company1 = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
        Company company2 = new Company("SPEED","speed@gmail.com", "ilovecookies",
                "2644125415","ΛΕΥΚΩΣΙΑΣ 66","ΠΑΤΡΑ","34785","163498317","GR3687254378963625");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(company1);
            em.persist(company2);
            tx.commit();
        });
    }

    @Test
    public void denySavingCompanyWithSameEmail() {
        Company company1 = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
        Company company2 = new Company("SPEED","avis@gmail.com", "ilovecookies",
                "2644125415","ΛΕΥΚΩΣΙΑΣ 66","ΠΑΤΡΑ","34785","999641227","GR3687254378963625");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(company1);
            em.persist(company2);
            tx.commit();
        });
    }

    @Test
    public void denySavingCompanyWithSameAfmAndEmail() {
        Company company1 = new Company("AVIS","avis@gmail.com", "password123", "2104578965",
                "ΠΑΤΗΣΙΩΝ 37","ΑΘΗΝΑ","12478","163498317","GR2514526358789654");
        Company company2 = new Company("SPEED","avis@gmail.com", "ilovecookies",
                "2644125415","ΛΕΥΚΩΣΙΑΣ 66","ΠΑΤΡΑ","34785","163498317","GR3687254378963625");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(company1);
            em.persist(company2);
            tx.commit();
        });
    }

    // policy
    @Test
    public void validatePolicySaved() {
        LinkedHashMap<Integer, Float> mileage_scale = new LinkedHashMap<Integer, Float>();
        mileage_scale.put(100, 0.10f);
        mileage_scale.put(200, 0.20f);
        mileage_scale.put(300, 0.30f);

        LinkedHashMap<DamageType, Float> damage_type = new LinkedHashMap<DamageType, Float>();
        damage_type.put(DamageType.Glasses,50f);
        damage_type.put(DamageType.Machine,30f);

        ChargingPolicy testPolicy = new ChargingPolicy(mileage_scale,damage_type);

        //query c.policy
        ChargingPolicy firstStored = (ChargingPolicy) em.createQuery("select c.policy from Company c").getResultList().get(0);

        assertEquals(testPolicy, firstStored);
    }

    //vehicles

}