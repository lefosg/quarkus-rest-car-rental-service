package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.util.Fixture;
import org.util.Money;
import org.util.VehicleState;
import org.util.VehicleType;

import static org.junit.jupiter.api.Assertions.*;
import static org.util.Fixture.createVehicleRepresentation;

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
        Mockito.when(fleetService.changeVehicleState(existingVehId,VehicleState.Rented)).thenReturn(true);
        Mockito.when(fleetService.changeVehicleState(nonExistingVehId,VehicleState.Available)).thenReturn(false);

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
        assertTrue(fleetService.changeVehicleState(existingVehId,VehicleState.Rented));
    }

    @Test
    void makeNonExistingVehicleRented() {
        assertFalse(fleetService.changeVehicleState(nonExistingVehId,VehicleState.Available));
    }

}