package org.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.Company;
import org.domain.TechnicalCheck;
import org.domain.Vehicle;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class TechnicalCheckRepositoryTest extends IntegrationBase{

    @Inject
    TechnicalCheckRepository technicalCheckRepository;

   // @Test
    void findByDamageType(){
        List<TechnicalCheck> technicalChecks = technicalCheckRepository.findByDamageType(null);

    }
}
