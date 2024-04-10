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

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TechnicalCheckServiceTest {

    Integer technicalCheckId = 5000;
    Integer existingVehicle = 3007;
    Integer nonExistingVehicle = 9000;

    @InjectMock
    FleetService fleetService;

    @Inject
    TechnicalCheckService technicalCheckService;

    @BeforeEach
    void setup() {
        Mockito.when(fleetService.vehicleById(existingVehicle)).thenReturn(createVehicleRepresentation(existingVehicle));
        Mockito.when(fleetService.vehicleById(nonExistingVehicle)).thenReturn(new VehicleRepresentation());
    }

    @Test
    void doTechnicalCheckExistingVehicle() {

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