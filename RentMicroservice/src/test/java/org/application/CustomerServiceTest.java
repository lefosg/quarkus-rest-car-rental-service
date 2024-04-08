package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import org.mockito.Mockito;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
public class CustomerServiceTest {
    Integer existingCustomer = 1000;
    Integer nonExistingCustomer = 4444;

    CustomerRepresentation customer;
    @InjectMock
    UserManagementService userManagementService;
    @Inject
    RentService rentService;

    @BeforeEach
    public void setup(){
        when(userManagementService.customerExists(existingCustomer)).thenReturn(true);
        when(userManagementService.customerExists(nonExistingCustomer)).thenReturn(false);
        customer = createCustomerRepresentation(existingCustomer);
        when(userManagementService.customerById(existingCustomer)).thenReturn(customer);
    }

    @Test
    public void getExistingCustomer(){assertTrue(rentService.customerExist(existingCustomer));}

    @Test
    public void getNonExistingCustomer(){assertFalse(rentService.customerExist(nonExistingCustomer));}
    
    @Test
    public void getCustomerRepresentationTest(){
        assertEquals(customer.id,rentService.returnCustomerWithId(existingCustomer).id);
        assertEquals(customer.city,rentService.returnCustomerWithId(existingCustomer).city);
        assertEquals(customer.street,rentService.returnCustomerWithId(existingCustomer).street);
        assertEquals(customer.holderName,rentService.returnCustomerWithId(existingCustomer).holderName);
        assertEquals(customer.surname,rentService.returnCustomerWithId(existingCustomer).surname);
        assertEquals(customer.AFM,rentService.returnCustomerWithId(existingCustomer).AFM);
        assertEquals(customer.email,rentService.returnCustomerWithId(existingCustomer).email);
        assertEquals(customer.password,rentService.returnCustomerWithId(existingCustomer).password);
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
}


