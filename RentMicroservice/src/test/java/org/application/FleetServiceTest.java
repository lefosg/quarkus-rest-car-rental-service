package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.util.Money;
import org.util.VehicleState;
import org.util.VehicleType;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class FleetServiceTest {

    Integer existingVehId = 3000;
    Integer nonExistingVehId = 9000;
    VehicleRepresentation vehicleRepresentation = createVehicleRepresentation(existingVehId);

    @InjectMock
    FleetService fleetService;

    @BeforeEach
    public void setup() {
        Mockito.when(fleetService.vehicleExists(existingVehId)).thenReturn(true);
        Mockito.when(fleetService.vehicleExists(nonExistingVehId)).thenReturn(false);
        Mockito.when(fleetService.vehicleById(existingVehId)).thenReturn(createVehicleRepresentation(existingVehId));
        Mockito.when(fleetService.vehicleById(nonExistingVehId)).thenReturn(new VehicleRepresentation());
        Mockito.when(fleetService.vehicleById(null)).thenReturn(new VehicleRepresentation());
        Mockito.when(fleetService.changeVehicleState(existingVehId)).thenReturn(true);
        Mockito.when(fleetService.changeVehicleState(nonExistingVehId)).thenReturn(false);

    }

    @Test
    void assertVehicleExists() {
        assertTrue(fleetService.vehicleExists(existingVehId));
    }

    @Test
    void assertVehicleNotExists() {
        assertFalse(fleetService.vehicleExists(nonExistingVehId));
    }

    @Test
    void getExistingVehicle() {
        assertEquals(existingVehId, fleetService.vehicleById(existingVehId).id);
    }

    @Test
    void getNonExistingVehicle() {
        assertNull(fleetService.vehicleById(nonExistingVehId).id);
    }

    @Test
    void getVehicleNullId() {
        assertNull(fleetService.vehicleById(nonExistingVehId).id);
    }

    @Test
    void makeExistingVehicleRented() {
        assertTrue(fleetService.changeVehicleState(existingVehId));
    }

    @Test
    void makeNonExistingVehicleRented() {
        assertFalse(fleetService.changeVehicleState(nonExistingVehId));
    }

    private VehicleRepresentation createVehicleRepresentation(Integer id) {
        VehicleRepresentation representation = new VehicleRepresentation();
        representation.id = id;
        representation.manufacturer = "AUDI";
        representation.model = "A7";
        representation.year = 2021;
        representation.miles = 100000;
        representation.plateNumber = "MMA-8745";
        representation.vehicleType = VehicleType.Sedan;
        representation.vehicleState = VehicleState.Rented;
        representation.fixedCharge = new Money(70);
        representation.companyId = 2000;
        return representation;
    }
}