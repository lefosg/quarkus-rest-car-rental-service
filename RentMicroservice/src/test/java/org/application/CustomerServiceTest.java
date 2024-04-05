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
public class CustomerServiceTest {
    Integer existingCustomer = 1000;
    Integer nonExistingCustomer = 4444;

    @InjectMock
    UserManagementService userManagementService;
    @Inject
    RentService rentService;

    @BeforeEach
    public void setup(){
        Mockito.when(userManagementService.customerExists(existingCustomer)).thenReturn(true);
        Mockito.when(userManagementService.customerExists(nonExistingCustomer)).thenReturn(false);
    }

    @Test
    public void getExistingVehicle(){assertTrue(rentService.customerExist(existingCustomer));}

    @Test
    public void getNonExistingVehicle(){assertFalse(rentService.customerExist(nonExistingCustomer));}

}
