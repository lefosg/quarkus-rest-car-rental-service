package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.infastructure.service.fleetManagament.representation.VehicleRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.Money;
import org.util.VehicleState;
import org.util.VehicleType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
public class UserServiceTest {
    @Inject
    UserService userService;

     @InjectMock
     FleetService fleetService;

    Integer existingCompany = 2000;
    Integer nonExistingCompany = 2222;

    @BeforeEach
    public void setup(){
        when(userService.getFleet(existingCompany)).thenReturn(createVehicleRepresentation(existingCompany));
        when(userService.getFleet(nonExistingCompany)).thenReturn(new ArrayList<>());
    }

     @Test
     public void getExistingCompanyVehicles(){
         assertEquals(userService.getFleet(existingCompany).get(0).id,createVehicleRepresentation(existingCompany).get(0).id);
     }

    @Test
    public void getNonExistingCompanyVehicles(){
        assertEquals(userService.getFleet(nonExistingCompany),new ArrayList<>());
    }


    private List<VehicleRepresentation> createVehicleRepresentation(Integer id) {
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
        representation.companyId = 2000;
        List<VehicleRepresentation> vehicles = new ArrayList<>();
        vehicles.add(representation);
        return vehicles;
    }

}
