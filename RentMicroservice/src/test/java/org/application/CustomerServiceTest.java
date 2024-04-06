package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
public class CustomerServiceTest {
    Integer existingCustomer = 1000;
    Integer nonExistingCustomer = 4444;
    CustomerRepresentation existingCustomerRepresentation;
    @InjectMock
    UserManagementService userManagementService;
    @Inject
    RentService rentService;

    @BeforeEach
    public void setup(){
        when(userManagementService.customerExists(existingCustomer)).thenReturn(true);
        when(userManagementService.customerExists(nonExistingCustomer)).thenReturn(false);
//        existingCustomerRepresentation = userManagementService.customerById(existingCustomer);
//        //when(existingCustomerRepresentation).thenReturn(userManagementService.customerById(existingCustomer));
    }

    @Test
    public void getExistingCustomer(){assertTrue(rentService.customerExist(existingCustomer));}

    @Test
    public void getNonExistingCustomer(){assertFalse(rentService.customerExist(nonExistingCustomer));}

//   todo den ksero pos na to kano edo
    
//    @Test
//    public void getCustomerRepresentationTest(){
//        CustomerRepresentation myCustomer = rentService.returnCustomerWithId(existingCustomer);
//        assertEquals(existingCustomerRepresentation.id,myCustomer.id);
//        assertEquals(existingCustomerRepresentation.name,myCustomer.name);
//        assertEquals(existingCustomerRepresentation.holderName,myCustomer.holderName);
//        assertEquals(existingCustomerRepresentation.surname,myCustomer.surname);
//        assertEquals(existingCustomerRepresentation.street,myCustomer.street);
//        assertEquals(existingCustomerRepresentation.email,myCustomer.email);
//        assertEquals(existingCustomerRepresentation.AFM,myCustomer.AFM);
//        assertEquals(existingCustomerRepresentation.city,myCustomer.city);
//    }

}
