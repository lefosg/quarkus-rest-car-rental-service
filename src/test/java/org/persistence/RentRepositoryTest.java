package org.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.domain.Rent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RentRepositoryTest {

    @Inject
    RentRepository rentRepository;

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
    void  findByMonthInvalid() {
        List<Rent> rents = rentRepository.findByMonth(13);
        assertEquals(2, rents.size());
    }

}