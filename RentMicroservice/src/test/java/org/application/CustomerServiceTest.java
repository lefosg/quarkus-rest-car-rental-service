package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashMap;

import org.mockito.Mockito;
import org.util.*;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@QuarkusTest
public class CustomerServiceTest {
    Integer existingCustomer = 1000;
    Integer nonExistingCustomer = 4444;
    Integer companyId = 1000;
    CustomerRepresentation customer;
    @InjectMock
    UserManagementService userManagementService;
    @InjectMock
    TechnicalCheckService technicalCheckService;
    @InjectMock
    FleetService fleetService;
    @Inject
    RentService rentService;

    @BeforeEach
    public void setup(){
        when(userManagementService.customerExists(existingCustomer)).thenReturn(true);
        when(userManagementService.customerExists(nonExistingCustomer)).thenReturn(false);
        customer = createCustomerRepresentation(existingCustomer);
        when(userManagementService.customerById(existingCustomer)).thenReturn(customer);
        when(userManagementService.customerById(nonExistingCustomer)).thenReturn(new CustomerRepresentation());
        when(userManagementService.customerById(null)).thenReturn(new CustomerRepresentation());
        when(userManagementService.pay(existingCustomer,companyId, 100,100)).thenReturn(true);
    }

    @Test
    public void assertCustomerExists(){assertTrue(rentService.customerExist(existingCustomer));}

    @Test
    public void assertCustomerNotExists(){assertFalse(rentService.customerExist(nonExistingCustomer));}

    @Test
    public void getCustomerRepresentationTest() {
        assertEquals(customer.id, userManagementService.customerById(existingCustomer).id);
        assertEquals(customer.city, userManagementService.customerById(existingCustomer).city);
        assertEquals(customer.street, userManagementService.customerById(existingCustomer).street);
    }

    @Test
    public void getNonExistingCustomer(){
        assertNull(userManagementService.customerById(nonExistingCustomer).id);
    }

    @Test
    public void getCustomerWithNullId() {
        assertNull(userManagementService.customerById(null).id);
    }

    @Test
    public void payTest() {
        assertTrue(userManagementService.pay(existingCustomer, companyId, 100, 100));
    }

    @Test
    public void testGetAllCosts(){
        HashMap<String, Float> incomingCosts = new HashMap<>();
        float miles = 50;
        incomingCosts.put(Constants.fixedCost, 50f);
        incomingCosts.put(Constants.mileageCost, 50f);
        incomingCosts.put(Constants.damageCost, 0f);
        Mockito.when(technicalCheckService.doTechnicalCheck(3000, 1500)).thenReturn(DamageType.NoDamage);
        Mockito.when(fleetService.vehicleById(3000)).thenReturn(createVehicleRepresentation(3000));
        Mockito.when(userManagementService.getAllCosts(miles, DamageType.NoDamage, 2000)).thenReturn(incomingCosts);

        assertEquals(50f, incomingCosts.get(Constants.fixedCost));
        assertEquals(50f, incomingCosts.get(Constants.mileageCost));
        assertEquals(0f, incomingCosts.get(Constants.damageCost));
    }
    private CustomerRepresentation createCustomerRepresentation(Integer id) {
        CustomerRepresentation representation = new CustomerRepresentation();
        representation.id = id;
        representation.name = "ΙΩΑΝΝΗΣ";
        representation.email = "evangellou@gmail.com";
        representation.password = "johnjohn";
        representation.phone = "6941603677";
        representation.street = "ΛΕΥΚΑΔΟΣ 22";
        representation.city = "ΑΘΗΝΑ";
        representation.zipcode = "35896";
        representation.AFM = "166008282";
        representation.surname = "ΕΥΑΓΓΕΛΟΥ";
        representation.expirationDate = LocalDate.of(2027,11,26).toString();
        representation.number = "7894665213797564";
        representation.holderName = "ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ";
        return representation;
    }

    private VehicleRepresentation createVehicleRepresentation(Integer id) {
        VehicleRepresentation representation = new VehicleRepresentation();
        representation.id = id;
        representation.manufacturer = "TOYOTA";
        representation.model = "YARIS";
        representation.year = 2015;
        representation.miles = 100000;
        representation.plateNumber = "YMB-6325";
        representation.vehicleType = VehicleType.Hatchback;
        representation.vehicleState = VehicleState.Available;
        representation.fixedCharge = new Money(30);
        representation.countDamages=0;
        representation.countOfRents=0;
        representation.companyId = 2000;
        return representation;
    }
}


