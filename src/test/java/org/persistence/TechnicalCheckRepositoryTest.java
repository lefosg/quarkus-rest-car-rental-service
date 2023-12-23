package org.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.Company;
import org.domain.TechnicalCheck;
import org.domain.Vehicle;
import org.junit.jupiter.api.Test;
import org.util.DamageType;
import org.util.IntegrationBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TechnicalCheckRepositoryTest extends IntegrationBase{

    @Inject
    TechnicalCheckRepository technicalCheckRepository;

   @Test
    void findByDamageTypeNull(){
        List<TechnicalCheck> technicalChecks = technicalCheckRepository.findByDamageType(null);
        assertEquals(2, technicalChecks.size());

        }

    @Test
    void findByDamageType1() {
        List<TechnicalCheck> technicalChecks = technicalCheckRepository.findByDamageType(DamageType.NoDamage);
        assertEquals(1, technicalChecks.size());
    }

    @Test
    void findByDamageType2() {
        List<TechnicalCheck> technicalChecks = technicalCheckRepository.findByDamageType(DamageType.Machine);
        assertEquals(0, technicalChecks.size());
    }

}

