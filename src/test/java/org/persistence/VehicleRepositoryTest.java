package org.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.domain.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class VehicleRepositoryTest {

    @Inject
    VehicleRepository vehicleRepository;

    @Test
    void findByManufacturer() {
        List<Vehicle> vehicles = vehicleRepository.findByManufacturer("TOYOTA");
        assertEquals(2, vehicles.size());
    }

    @Test
    void findByManufacturerEmptyString() {
        List<Vehicle> vehicles = vehicleRepository.findByManufacturer("");
        assertEquals(11, vehicles.size());
    }

    @Test
    void findByManufacturerNull() {
        List<Vehicle> vehicles = vehicleRepository.findByManufacturer(null);
        assertEquals(11, vehicles.size());
    }
}