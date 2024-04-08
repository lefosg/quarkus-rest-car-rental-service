package org.infastructure.rest.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.NotFoundException;
import org.application.UserManagementService;
import org.infastructure.rest.ApiPath;
import org.infastructure.rest.Representation.VehicleRepresentation;
import org.infastructure.service.user_management.representation.CompanyRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.util.*;
import java.util.List;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class VehicleResourceTest extends IntegrationBase {

    Integer vehId;
    String vehManufacturer;
    String vehModel;
    Integer existingCompany = 2000;
    Integer nonExistingCompany = 5000;

    @InjectMock
    UserManagementService userService;

    @BeforeEach
    public void setup() {
        vehId = 3000;
        vehManufacturer = "TOYOTA";
        vehModel = "YARIS";

        Mockito.when(userService.companyExists(existingCompany))
                .thenReturn(true);
        Mockito.when(userService.companyExists(nonExistingCompany))
                .thenReturn(false);
        Mockito.when(userService.getCompany(existingCompany))
                .thenReturn(createCompanyRepresentation(existingCompany));
        Mockito.when(userService.getCompany(nonExistingCompany))
                .thenThrow(new NotFoundException("[!] GET /company/"+nonExistingCompany+"\n\tCould not find company with id " + nonExistingCompany));
    }

    // ---------- GET ----------

    //todo fix
    @Test
    public void listAllVehicles() {
        List<VehicleRepresentation> vehicles = when().get(ApiPath.Root.VEHICLE)
                .then()
                .extract()
                .as(new TypeRef<List<VehicleRepresentation>>() {});

        assertEquals(11, vehicles.size());
    }

    @Test
    public void listVehicleIdValid() {
        VehicleRepresentation vehicle = when().get(ApiPath.Root.VEHICLE + "/" + vehId)
                .then()
                .extract()
                .as(VehicleRepresentation.class);

        assertEquals(vehId, vehicle.id);
    }

    @Test
    public void listVehicleIdValidIdInvalid() {
        when().get( ApiPath.Root.VEHICLE + "/" + 3020) //id 3020 not existent
                .then()
                .statusCode(404);
    }

    @Test
    public void listCompanyOfVehicleValidId() {
        CompanyRepresentation representation = when().get(ApiPath.Root.VEHICLE + "/" + vehId + "/company")
                .then()
                .extract()
                .as(CompanyRepresentation.class);

        assertEquals(2000, representation.id);
    }

    @Test
    public void listCompanyOfVehicleInvalidId() {
        when().get( ApiPath.Root.VEHICLE + "/" + 3020 + "/company") //id 3020 not existent
            .then()
            .statusCode(404);
    }

    @Test
    public void listWrongCompanyForValidVehicle() {
        //just for this test, on purpose make a right call return an exception
        Mockito.when(userService.getCompany(existingCompany))
                .thenThrow(new NotFoundException("[!] GET /company/"+existingCompany+"\n\tCould not find company with id " + existingCompany));

        CompanyRepresentation representation = when().get(ApiPath.Root.VEHICLE + "/" + vehId + "/company")
            .then()
            .extract()
            .as(CompanyRepresentation.class);

        assertEquals((new CompanyRepresentation()).id, representation.id);
        assertNull(representation.id);
    }

    // ---------- PUT ----------

    @Test
    public void createVehicleValid() {
        VehicleRepresentation representation = createVehicleRepresentation((Integer)3011);

        VehicleRepresentation created = given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(ApiPath.Root.VEHICLE)
                .then().statusCode(201).header("Location", Constants.API_ROOT + ApiPath.Root.VEHICLE + "/" + representation.id)
                .extract().as(VehicleRepresentation.class);

        assertEquals(3011, created.id);
    }

    @Test
    public void createVehicleIdInvalid() {
        VehicleRepresentation representation = createVehicleRepresentation((Integer) 3010);

        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(ApiPath.Root.VEHICLE)
                .then().statusCode(404);
    }

    @Test
    public void createVehicleIdNull() {
        VehicleRepresentation representation = createVehicleRepresentation(null);

        given()
            .contentType(ContentType.JSON)
            .body(representation)
            .when().put(ApiPath.Root.VEHICLE)
            .then().statusCode(404);
    }

    @Test
    public void updateVehicleValid() {
        //get the resource
        VehicleRepresentation representation = when().get(ApiPath.Root.VEHICLE + "/" + vehId)
                .then().statusCode(200).extract().as(VehicleRepresentation.class);

        assertEquals(vehId, representation.id);

        //make the update
        int newYear = 2020;
        representation.year = newYear;

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(ApiPath.Root.VEHICLE + "/" + vehId)
                .then().statusCode(204);

        //get the resource again to validate
        VehicleRepresentation updated = when().get(ApiPath.Root.VEHICLE + "/" + vehId)
                .then().statusCode(200).extract().as(VehicleRepresentation.class);

        assertEquals(vehId, updated.id);
        assertEquals(newYear, updated.year);
    }

    @Test
    public void updateVehicleInvalid() {
        //get the resource
        VehicleRepresentation representation = when().get(ApiPath.Root.VEHICLE + "/" + vehId)
                .then().statusCode(200).extract().as(VehicleRepresentation.class);

        assertEquals(vehId, representation.id);

        //make the update
        representation.year = 2020;

        //send the update to wrong endpoint, id mismatching
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(ApiPath.Root.VEHICLE + "/3020")
                .then().statusCode(404);

        //make another update, set id to null -> should return 404
        representation.id = null;
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(ApiPath.Root.VEHICLE + "/" + vehId)
                .then().statusCode(404);

        //make another update, set companyId to non-existing company -> should return 404
        representation.id = 3000;  //fix id from previous invalid update
        representation.companyId = nonExistingCompany;
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(ApiPath.Root.VEHICLE + "/" + vehId)
                .then().statusCode(404);
    }

    // ---------- DELETE ----------

    @Test
    public void deleteAllVehicles() {
        when().delete(ApiPath.Root.VEHICLE)
                .then().statusCode(200);

        when().get(ApiPath.Root.VEHICLE + "/"+3000)
                .then().statusCode(404);  //get must return 404
        when().get(ApiPath.Root.VEHICLE + "/"+3001)
                .then().statusCode(404);  //get must return 404

        //todo
        //when().get("/company/"+2000)
        //        .then().statusCode(200);  //getting a company should return 200

    }

    @Test
    public void deleteOneVehicleValid() {
        when().delete(ApiPath.Root.VEHICLE + "/" + vehId)
                .then().statusCode(200);

        when().get(ApiPath.Root.VEHICLE + "/" + vehId)
                .then().statusCode(404);  //get must return 404

        when().delete(ApiPath.Root.VEHICLE + "/" + 3015)  //id 3015 not in db
                .then().statusCode(404);

    }


    // ---------- misc ----------

    private VehicleRepresentation createVehicleRepresentation(Integer id) {
        VehicleRepresentation representation = new VehicleRepresentation();
        representation.id = id;
        representation.manufacturer = "MITSUBISHI";
        representation.model = "COLT";
        representation.year = 2006;
        representation.miles = 98000;
        representation.plateNumber = "XAX-1637";
        representation.vehicleType = VehicleType.Hatchback;
        representation.vehicleState = VehicleState.Available;
        representation.fixedCharge = new Money(30);
        representation.companyId = existingCompany;
        return representation;
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
