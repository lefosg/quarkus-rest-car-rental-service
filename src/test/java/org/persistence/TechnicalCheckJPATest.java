package org.persistence;

import org.domain.TechnicalCheck;
import org.domain.TechnicalCheckImpl;
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
