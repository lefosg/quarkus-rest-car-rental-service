package org.application;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.infastructure.service.user_management.representation.CompanyRepresentation;
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
        Mockito.when(userService.getCompany(existingCompany))
                .thenReturn(createCompanyRepresentation(existingCompany));
        Mockito.when(userService.getCompany(nonExistingCompany))
                .thenThrow(new NotFoundException("[!] GET /company/"+nonExistingCompany+"\n\tCould not find company with id " + nonExistingCompany));
    }

    @Test
    public void getExistingCompany() {
        assertTrue(vehicleService.companyExists(existingCompany));
    }

    @Test
    public void getNonExistingCompany() {
        assertFalse(vehicleService.companyExists(nonExistingCompany));
    }

    @Test
    public void getExistingCompanyRepresentation() {
        assertEquals(existingCompany, userService.getCompany(existingCompany).id);
    }

    @Test
    public void getNonExistingCompanyRepresentation() {
        assertThrows(NotFoundException.class, () -> {
            userService.getCompany(nonExistingCompany);
        });
    }


    private CompanyRepresentation createCompanyRepresentation(Integer id) {
        CompanyRepresentation representation = new CompanyRepresentation();
        representation.id = id;
        representation.name = "HOLYDAYCARS";
        representation.email = "holydaycrs@gmail.com";
        representation.phone = "2218603784";
        representation.street = "ΜΑΚΕΔΟΝΙΑΣ 87";
        representation.city = "ΘΕΣΣΑΛΟΝΙΚΗ";
        representation.zipcode = "47895";
        representation.password = "topcars123";
        representation.AFM = "998678010";
        representation.IBAN = "GR12564789652365";
        return representation;
    }


}