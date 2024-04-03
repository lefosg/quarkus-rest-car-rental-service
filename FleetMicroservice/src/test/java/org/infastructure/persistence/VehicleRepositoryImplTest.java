package org.infastructure.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.domain.Vehicle.Vehicle;
import org.domain.Vehicle.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;
import org.util.VehicleState;

import java.util.List;
import java.util.NoSuchElementException;

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
        assertEquals(0, vehicles.size());
    }

    @Test
    void findByManufacturerNull() {
        List<Vehicle> vehicles = vehicleRepository.findByManufacturer(null);
        assertEquals(0, vehicles.size());
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

    @Test
    void listAllVehicles() {
        assertEquals(11, vehicleRepository.listAllVehicles().size());
    }

    @Test
    void findById() {
        //find by id
        assertEquals(3000, vehicleRepository.findVehicleById(3000).getId());
        assertNull(vehicleRepository.findVehicleById(1234));

        //find by id optional
        assertEquals(3000, vehicleRepository.findVehicleByIdOptional(3000).get().getId());
        assertThrows(NoSuchElementException.class, () -> {
            vehicleRepository.findVehicleByIdOptional(1234).get().getId();
        });
    }

    @Test
    void findByManufacturerAndState() {
        assertEquals(2, vehicleRepository.findByManufacturerAndState("TOYOTA", VehicleState.Available).size());
        assertEquals(0, vehicleRepository.findByManufacturerAndState("TOYOTA", VehicleState.Rented).size());
        assertEquals(0, vehicleRepository.findByManufacturerAndState("TOYOTA", VehicleState.Service).size());
    }


}