package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;


@QuarkusTest
public class RentServiceTest {
    Integer existingVehicle = 3005;
    Integer nonExistingVehicle = 4444;

    @InjectMock
    FleetService fleetService;
    @Inject
    RentService rentService;

    @BeforeEach
    public void setup(){
        Mockito.when(fleetService.vehicleExists(existingVehicle)).thenReturn(true);
        Mockito.when(fleetService.vehicleExists(nonExistingVehicle)).thenReturn(false);
    }

    @Test
    public void getExistingVehicle(){assertTrue(rentService.rentedVehicleExist(existingVehicle));}

    @Test
    public void getNonExistingVehicle(){assertFalse(rentService.rentedVehicleExist(nonExistingVehicle));}
}
