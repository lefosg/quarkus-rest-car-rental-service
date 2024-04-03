package org.infastructure.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.Rents.RentRepository;
import org.domain.TechnicalCheck.TechnicalCheck;
import org.domain.TechnicalCheck.TechnicalCheckRepository;
import org.junit.jupiter.api.Test;
import org.util.DamageType;
import org.util.IntegrationBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class TechnicalCheckRepositoryImplTest extends IntegrationBase{

    @Inject
    TechnicalCheckRepository technicalCheckRepository;

    @Inject
    RentRepository rentRepository;

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

    @Test
    void listAll() {
       assertEquals(2, technicalCheckRepository.listAllTechnicalChecks().size());
    }

    @Test
    @Transactional
    void deleteAllTechnicalChecks() {
       technicalCheckRepository.deleteAllTechnicalChecks();
       assertEquals(0, technicalCheckRepository.listAllTechnicalChecks().size());
       assertEquals(2, rentRepository.listAllRents().size());
    }

    @Test
    @Transactional
    void deleteTechnicalCheckValid() {
        technicalCheckRepository.deleteTechnicalCheck(5000);
        List<TechnicalCheck> technicalChecks = technicalCheckRepository.listAllTechnicalChecks();
        assertEquals(1, technicalChecks.size());
        assertEquals(2, rentRepository.listAllRents().size());  //deleting one technical check should not delete the rent
    }

    @Test
    @Transactional
    void deleteTechnicalCheckInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            technicalCheckRepository.deleteTechnicalCheck(null);
        });

        assertThrows(NotFoundException.class, () -> {
            technicalCheckRepository.deleteTechnicalCheck(5005);  //5005 not in db
        });
    }

}

