package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.util.Money;
import org.util.VehicleState;
import org.util.VehicleType;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@QuarkusTest
public class RentServiceTest {
    Integer existingVehicle = 3005;
    Integer nonExistingVehicle = 4444;

    VehicleRepresentation vehicle;
    @InjectMock
    FleetService fleetService;
    @Inject
    RentService rentService;

    @BeforeEach
    public void setup(){
        Mockito.when(fleetService.vehicleExists(existingVehicle)).thenReturn(true);
        Mockito.when(fleetService.vehicleExists(nonExistingVehicle)).thenReturn(false);
        vehicle = createVehicleRepresentation(existingVehicle);
        when(fleetService.vehicleById(existingVehicle)).thenReturn(vehicle);
    }

    @Test
    public void getExistingVehicle(){assertTrue(rentService.rentedVehicleExist(existingVehicle));}

    @Test
    public void getNonExistingVehicle(){assertFalse(rentService.rentedVehicleExist(nonExistingVehicle));}

    @Test
    public void getCustomerRepresentationTest(){
        assertEquals(vehicle.id,rentService.returnVehicleWithId(existingVehicle).id);
        assertEquals(vehicle.manufacturer,rentService.returnVehicleWithId(existingVehicle).manufacturer);
        assertEquals(vehicle.model,rentService.returnVehicleWithId(existingVehicle).model);
        assertEquals(vehicle.year,rentService.returnVehicleWithId(existingVehicle).year);
        assertEquals(vehicle.miles,rentService.returnVehicleWithId(existingVehicle).miles);
        assertEquals(vehicle.plateNumber,rentService.returnVehicleWithId(existingVehicle).plateNumber);
        assertEquals(vehicle.vehicleState,rentService.returnVehicleWithId(existingVehicle).vehicleState);
        assertEquals(vehicle.vehicleType,rentService.returnVehicleWithId(existingVehicle).vehicleType);
        assertEquals(vehicle.fixedCharge,rentService.returnVehicleWithId(existingVehicle).fixedCharge);
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
        representation.vehicleState = VehicleState.Available;
        representation.fixedCharge = new Money(70);
        return representation;
    }
}
