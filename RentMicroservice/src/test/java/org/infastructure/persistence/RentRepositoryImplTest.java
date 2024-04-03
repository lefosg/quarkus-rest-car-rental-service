package org.infastructure.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.Rents.Rent;
import org.domain.Rents.RentRepository;
import org.domain.TechnicalCheck.TechnicalCheckRepository;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RentRepositoryImplTest extends IntegrationBase {

    Integer rentId = 4000;

    @Inject
    RentRepository rentRepository;

    @Inject
    TechnicalCheckRepository technicalCheckRepository;

    @Test
    void listAllRents() {
        List<Rent> rents = rentRepository.findByMonth(0);
        assertEquals(2, rents.size());
    }

    @Test
    void findByMonthValid() {
        List<Rent> rents = rentRepository.findByMonth(12);
        assertEquals(2, rents.size());
    }

    @Test
    void findByMonthInvalid() {
        List<Rent> rents = rentRepository.findByMonth(13);
        assertEquals(2, rents.size());
    }

    @Test
    @Transactional
    void deleteAllRents() {
        rentRepository.deleteAllRents();
        assertEquals(0, rentRepository.listAllRents().size());
        assertEquals(0, technicalCheckRepository.listAllTechnicalChecks().size());  //there shouldn't be any technical checks left
    }

    @Test
    @Transactional
    void deleteOneRentValid() {
        rentRepository.deleteRent(rentId);
        List<Rent> rents = rentRepository.listAllRents();
        assertEquals(1, rents.size());
        assertNull(rentRepository.findRentById(rentId));
    }

    @Test
    @Transactional
    void deleteOneRentInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            rentRepository.deleteRent(null);
        });

        assertThrows(NotFoundException.class, () -> {
            rentRepository.deleteRent(2002);
        });
    }

}