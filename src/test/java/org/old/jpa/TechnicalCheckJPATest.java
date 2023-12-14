package org.old.jpa;

import org.domain.TechnicalCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TechnicalCheckJPATest extends JPATest{

    @Test
    public void listTechnicalChecks() {
        List<TechnicalCheck> technicalChecks = em.createQuery("select tc from TechnicalCheck tc").getResultList();
        assertEquals(2, technicalChecks.size());

    }
}
