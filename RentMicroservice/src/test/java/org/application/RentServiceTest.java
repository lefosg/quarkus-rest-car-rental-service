package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.util.*;

import java.util.HashMap;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@QuarkusTest
public class RentServiceTest {
    Integer existingVehicleId = 3000;
    Integer nonExistingVehicleId = 4444;
    Integer existingTechnicalCheckId = 5000;
    Integer nonExistingTechnicalCheckId = 9000;
    Integer existingCustomerId = 1000;
    Integer nonExistingCustomerId = 8000;

    //gia pay
    Integer existingVehicle = 3005;
    Integer nonExistingVehicle = 4444;
    Integer existingCustomer = 3005;
    Integer nonExistingCustomer = 4444;
    Integer existingCompany = 2000;
    Integer nonExistingCompany = 4404;

    VehicleRepresentation vehicle;
    VehicleRepresentation changedVehicle;
    @InjectMock
    FleetService fleetService;
    @Inject
    RentService rentService;
    @InjectMock
    TechnicalCheckService technicalCheckService;
    @InjectMock
    UserManagementService userManagementService;

    @BeforeEach
    public void setup(){
        Mockito.when(fleetService.vehicleExists(existingVehicleId)).thenReturn(true);
        Mockito.when(fleetService.vehicleExists(nonExistingVehicleId)).thenReturn(false);
        vehicle = Fixture.createVehicleRepresentation(existingVehicleId);
        changedVehicle = vehicle;
        changedVehicle.vehicleState = VehicleState.Rented;
        when(fleetService.vehicleById(existingVehicleId)).thenReturn(vehicle);


        when(fleetService.vehicleById(nonExistingVehicle)).thenReturn(null);
        when(fleetService.changeVehicleState(existingVehicle,VehicleState.Rented)).thenReturn(true);

        when(userManagementService.pay(existingCustomer, existingCompany, 1000,1000)).thenReturn(true);
        when(userManagementService.pay(nonExistingCustomer, nonExistingCompany, 1000,1000)).thenReturn(false);

    }

    @Test
    public void getExistingVehicle(){assertTrue(rentService.rentedVehicleExist(existingVehicleId));}

    @Test
    public void getNonExistingVehicle(){assertFalse(rentService.rentedVehicleExist(nonExistingVehicleId));}

    @Test
    public void changeVehicleStateTest(){
        rentService.changeVehicleState(existingVehicleId,VehicleState.Rented);
        rentService.changeVehicleState(nonExistingVehicle,VehicleState.Available);
        assertEquals(changedVehicle,rentService.returnVehicleWithId(existingVehicleId));
        assertEquals(VehicleState.Rented,changedVehicle.vehicleState);
    }

    @Test
    public void getCustomerRepresentationTest(){
        assertEquals(vehicle.id,rentService.returnVehicleWithId(existingVehicleId).id);
        assertEquals(vehicle.manufacturer,rentService.returnVehicleWithId(existingVehicleId).manufacturer);
        assertEquals(vehicle.model,rentService.returnVehicleWithId(existingVehicleId).model);
        assertEquals(vehicle.year,rentService.returnVehicleWithId(existingVehicleId).year);
        assertEquals(vehicle.miles,rentService.returnVehicleWithId(existingVehicleId).miles);
        assertEquals(vehicle.plateNumber,rentService.returnVehicleWithId(existingVehicleId).plateNumber);
        assertEquals(vehicle.vehicleState,rentService.returnVehicleWithId(existingVehicleId).vehicleState);
        assertEquals(vehicle.vehicleType,rentService.returnVehicleWithId(existingVehicleId).vehicleType);
        assertEquals(vehicle.fixedCharge,rentService.returnVehicleWithId(existingVehicleId).fixedCharge);
    }

    //test pay
    @Test
    public void payTestValidVehicle() {
        VehicleRepresentation vehicle2 = Fixture.createVehicleRepresentation(existingVehicle);
        when(fleetService.vehicleById(existingVehicle)).thenReturn(vehicle2);
        when(userManagementService.pay(existingCustomer, existingCompany, 1000,1000)).thenReturn(true);
        assertTrue(rentService.pay(existingCustomer,existingVehicle,1000,1000));
    }
    @Test
    public void payTestInValidVehicle() {
        assertFalse(rentService.pay(nonExistingCustomer,nonExistingVehicle,1000,1000));
    }

    // test calculate all costs
    @Test
    void calculateCosts() {
        float miles = 50;
        HashMap<String, Float> costsToReturn = new HashMap<>();
        costsToReturn.put(Constants.fixedCost, 50f);
        costsToReturn.put(Constants.mileageCost, 50f);
        costsToReturn.put(Constants.damageCost, 0f);
        Mockito.when(technicalCheckService.doTechnicalCheck(existingVehicleId, existingTechnicalCheckId)).thenReturn(DamageType.NoDamage);
        Mockito.when(fleetService.vehicleById(existingVehicleId)).thenReturn(Fixture.createVehicleRepresentation(existingCustomerId));
        Mockito.when(userManagementService.getAllCosts(miles, DamageType.NoDamage, 2000)).thenReturn(costsToReturn);

        HashMap<String, Float> costs = rentService.calculateCosts(existingCustomerId, existingVehicleId, miles);
        assertEquals(50f, costs.get(Constants.fixedCost));
        assertEquals(50f, costs.get(Constants.mileageCost));
        assertEquals(0f, costs.get(Constants.damageCost));
    }

    @Test
    void calculateCostsInvalid_doTechnicalCheck() {
        float miles = 50f;
        //check that no rent is found
        assertThrows(NullPointerException.class, () -> {
            rentService.calculateCosts(nonExistingCustomerId, existingVehicleId, miles);
            rentService.calculateCosts(existingCustomerId, nonExistingVehicleId, miles);
        });

        //check that doTechnicalCheck returns null
        Mockito.when(technicalCheckService.doTechnicalCheck(existingVehicleId, existingTechnicalCheckId)).thenReturn(null);
        Mockito.when(technicalCheckService.doTechnicalCheck(existingVehicleId, nonExistingTechnicalCheckId)).thenReturn(null);
        assertNull(rentService.calculateCosts(existingCustomerId, existingVehicleId, miles));
    }

    @Test
    void calculateCostsInvalid_calculcateAllCosts() {
        float miles = 50f;
        //check that calculateAllCosts::fleet returns null
        Mockito.when(fleetService.vehicleById(existingVehicleId)).thenReturn(new VehicleRepresentation());
        assertNull(rentService.calculateCosts(existingCustomerId, existingVehicleId, miles));
        //check that calculateAllCosts::user.getAllCosts returns null
        Mockito.when(fleetService.vehicleById(existingVehicleId)).thenReturn(Fixture.createVehicleRepresentation(existingVehicleId));
        Mockito.when(userManagementService.getAllCosts(miles, DamageType.NoDamage, 2000)).thenReturn(null);
        assertNull(rentService.calculateCosts(existingCustomerId, existingVehicleId, miles));
    }



}
