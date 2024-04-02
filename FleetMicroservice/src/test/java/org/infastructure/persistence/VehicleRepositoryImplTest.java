package org.infastructure.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.Vehicle.Vehicle;
import org.domain.Vehicle.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class VehicleRepositoryImplTest extends IntegrationBase {

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

    @Test
    @Transactional
    void deleteAllCompanies() {
        vehicleRepository.deleteAllVehicles();
        assertEquals(0, vehicleRepository.listAllVehicles().size());
    }

    @Test
    @Transactional
    void deleteOneCompanyValid() {
        vehicleRepository.deleteVehicle(3000);
        List<Vehicle> vehicles = vehicleRepository.listAllVehicles();
        assertEquals(10, vehicles.size());
        assertNull(vehicleRepository.findVehicleById(3000));
    }

    @Test
    @Transactional
    void deleteOneCompanyInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            vehicleRepository.deleteVehicle(null);
        });

        assertThrows(NotFoundException.class, () -> {
            vehicleRepository.deleteVehicle(3015);  //id 3015 not in db
        });
    }
}