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


}