package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.jboss.resteasy.links.impl.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.util.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TechnicalCheckServiceTest {

    Integer technicalCheckId = 5000;
    Integer nonExistingTechnicalCheckId = 9000;
    Integer existingVehicle = 3007;
    Integer nonExistingVehicle = 9000;

    @InjectMock
    FleetService fleetService;

    @Inject
    TechnicalCheckService technicalCheckService;

    @BeforeEach
    void setup() {
        Mockito.when(fleetService.vehicleById(existingVehicle)).thenReturn(Fixture.createVehicleRepresentation(existingVehicle));
        Mockito.when(fleetService.vehicleById(nonExistingVehicle)).thenReturn(new VehicleRepresentation());
    }

    //@Test
    void doTechnicalCheckExistingVehicle() {
        DamageType damageType = technicalCheckService.doTechnicalCheck(existingVehicle, technicalCheckId);
        System.out.println(damageType);
        //0 damages and 0 rents should PROBABLY (most likely) return no damage, don't take this test seriously
        assertEquals(DamageType.NoDamage, damageType);
    }

    @Test
    void doTechnicalCheckNonExistingVehicle() {
        DamageType damageType = technicalCheckService.doTechnicalCheck(nonExistingVehicle, technicalCheckId);
        assertNull(damageType);
    }

    //why are there 2 NotFoundException classes in java lol
    @Test
    void doTechnicalCheckExistingVehicleInvalidTechnicalCheckId() {
        assertThrows(jakarta.ws.rs.NotFoundException.class, ()-> {
            DamageType type = technicalCheckService.doTechnicalCheck(existingVehicle, nonExistingTechnicalCheckId);
            System.out.println(type);
        });
    }

    @Test
    void doTechnicalCheckNonExistingVehicleInvalidTechnicalCheckId() {
        Mockito.when(fleetService.vehicleById(nonExistingVehicle)).thenReturn(Fixture.createVehicleRepresentation(nonExistingVehicle));
        assertThrows(jakarta.ws.rs.NotFoundException.class, ()-> {
            technicalCheckService.doTechnicalCheck(nonExistingVehicle, nonExistingTechnicalCheckId);
        });
    }
}