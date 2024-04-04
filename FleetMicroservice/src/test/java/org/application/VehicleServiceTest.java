package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class VehicleServiceTest {

    Integer existingCompany = 2000;
    Integer nonExistingCompany = 9000;

    @Inject
    VehicleService vehicleService;

    @InjectMock
    UserManagementService userService;

    @BeforeEach
    public void setup() {
        Mockito.when(userService.companyExists(existingCompany))
                .thenReturn(true);
        Mockito.when(userService.companyExists(nonExistingCompany))
                .thenReturn(false);
    }

    @Test
    public void getExistingCompany() {
        assertTrue(vehicleService.companyExists(existingCompany));
    }

    @Test
    public void getNonExistingCompany() {
        assertFalse(vehicleService.companyExists(nonExistingCompany));
    }

}