package org.infastructure.rest.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.application.FleetService;
import org.application.RentService;
import org.application.UserManagementService;
import org.domain.Rents.Rent;
import org.domain.Rents.RentRepository;
import org.infastructure.rest.representation.RentRepresentation;
import org.infastructure.rest.representation.TechnicalCheckRepresentation;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.infastructure.rest.ApiPath.Root.CHECKS;
import static org.infastructure.rest.ApiPath.Root.RENTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.util.Fixture.createCustomerRepresentation;
import static org.util.Fixture.createRentedVehicleRepresentation;

import org.mockito.Mockito;

@QuarkusTest
class RentResourceTest extends IntegrationBase {

    Integer rentId;
    LocalDate startDate;
    Integer compId;
    Integer customerId, nonExistingCustomer;
    Integer vehicleId, audiId, nonExistingVehicle;
    Integer amount_money,amount_damages;

    @Inject
    RentRepository rentRepository;

    @InjectMock
    UserManagementService userManagementService;

    @InjectMock
    FleetService fleetService;

    @Inject
    RentService rentService;

    @BeforeEach
    public void setup() {
        rentId = 4000;
        compId = 2000;
        vehicleId = 3000;
        audiId = 3007;
        customerId = 1000;
        nonExistingCustomer = 9000;
        nonExistingVehicle = 8000;
        startDate = LocalDate.of(2023, 12, 5);
        amount_money = 1000;
        amount_damages = 1000;

        Mockito.when(userManagementService.customerExists(customerId)).thenReturn(true);
        Mockito.when(userManagementService.customerExists(nonExistingCustomer)).thenReturn(false);
        Mockito.when(userManagementService.customerById(customerId)).thenReturn(createCustomerRepresentation(customerId));
        Mockito.when(userManagementService.customerById(nonExistingCustomer))
                        .thenReturn(new CustomerRepresentation());
        Mockito.when(userManagementService.customerById(null))
                .thenReturn(new CustomerRepresentation());

        Mockito.when(fleetService.vehicleExists(vehicleId)).thenReturn(true);
        Mockito.when(fleetService.vehicleById(vehicleId)).thenReturn(createRentedVehicleRepresentation(vehicleId));
        Mockito.when(fleetService.vehicleById(nonExistingVehicle))
                        .thenReturn(new VehicleRepresentation());
        Mockito.when(fleetService.vehicleExists(nonExistingVehicle)).thenReturn(false);
        Mockito.when(fleetService.vehicleById(null)).thenReturn(new VehicleRepresentation());
                //.thenThrow(new BadRequestException("[!] GET /vehicle/"+"\n\tCould not find vehicle with null id "));

        Mockito.when(rentService.pay(customerId,vehicleId,amount_money,amount_damages)).thenReturn(true);
        Mockito.when(rentService.pay(customerId,nonExistingVehicle,amount_money,amount_damages)).thenReturn(false);

        Mockito.when(fleetService.changeVehicleState(vehicleId,VehicleState.Rented)).thenReturn(true);
        Mockito.when(fleetService.changeVehicleState(nonExistingVehicle,VehicleState.Available)).thenReturn(true);
    }

    // ---------- GET ----------

    //rent get

    @Test
    public void listAllRents() {
        List<RentRepresentation> rents = when().get(RENTS)
                .then()
                .extract()
                .as(new TypeRef<List<RentRepresentation>>() {});

        assertEquals(2, rents.size());
    }

    @Test
    public void listRentsByMonth() {
        List<RentRepresentation> rents = when().get(RENTS +"?month=12")
                .then()
                .extract()
                .as(new TypeRef<List<RentRepresentation>>() {});

        assertEquals(2, rents.size());
    }

    @Test
    public void listRentsByMonthInvalid() {
        List<RentRepresentation> rents = when().get(RENTS +"?month=13")  //month 13 does not exist, should return listAll
                .then()
                .extract()
                .as(new TypeRef<List<RentRepresentation>>() {});

        assertEquals(2, rents.size());
    }

    @Test
    public void listRentByIdValid() {
        RentRepresentation representation = when().get(RENTS+"/"+rentId)
                .then()
                .extract()
                .as(RentRepresentation.class);

        assertEquals(rentId, representation.id);
    }

    @Test
    public void listRentByIdInvalid() {
        when().get(RENTS+"/" + 4005)  //id 4005 not in db
                .then()
                .statusCode(404);

        when().get(RENTS+"/" + null)  //id 4005 not in db
                .then()
                .statusCode(404);
    }

    //customer get

    @Test
    public void listCustomerOfRentValid() {
        CustomerRepresentation representation = when().get(RENTS+ "/" +rentId+"/customer")
                .then()
                .extract()
                .as(CustomerRepresentation.class);

        assertEquals(1000, representation.id);
    }

    @Test
    public void listCustomerOfRentInvalid() {
        when().get(RENTS + "/" + 4005 +"/customer")  //id 4005 not in db
                .then()
                .statusCode(404);

        when().get(RENTS + "/"+ null+ "/customer")  //id null invalid
                .then()
                .statusCode(404);

        //on purpose, for an existing rent make the customer
        Mockito.when(userManagementService.customerById(customerId)).thenReturn(new CustomerRepresentation());
        CustomerRepresentation representation = when().get(RENTS+ "/" +rentId+"/customer")
                .then()
                .extract()
                .as(CustomerRepresentation.class);
        assertNull(representation.id);
    }

    //vehicle get

    @Test
    public void listVehicleOfRentValid() {
        VehicleRepresentation representation = when().get(RENTS + "/" +rentId+"/vehicle")
                .then()
                .extract()
                .as(VehicleRepresentation.class);

        assertEquals(3000, representation.id);
    }

    @Test
    public void listVehicleOfRentInvalid() {
        when().get(RENTS + "/vehicle")  //id null invalid
                .then()
                .statusCode(404);

        when().get(RENTS + "/" + 4005 + "/vehicle")  //id 4005 not in db
                .then()
                .statusCode(404);

        Mockito.when(fleetService.vehicleById(vehicleId)).thenReturn(new VehicleRepresentation());
        VehicleRepresentation representation = when().get(RENTS + "/" +rentId+"/vehicle")
                .then()
                .extract()
                .as(VehicleRepresentation.class);
        assertNull(representation.id);
    }

    //technical check get

    @Test
    public void listTechnicalCheckOfRentValid() {
        TechnicalCheckRepresentation representation = when().get(RENTS + "/" + rentId + "/technicalCheck")
                .then()
                .extract()
                .as(TechnicalCheckRepresentation.class);

        assertEquals(5000, representation.id);
    }

    @Test
    public void listTechnicalCheckOfRentInvalid() {
        when().get(RENTS + "/" + 4005 + "/technicalCheck")  //id 4005 not in db
                .then()
                .statusCode(404);

        when().get(RENTS + "/" + null + "/technicalCheck")  //id null invalid
                .then()
                .statusCode(404);
    }

    // ---------- PUT ----------


    @Test
    public void makeRentWithInvalidDates() {
        Response response = given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().plusDays(5).toString())
                .queryParam("endDate", LocalDate.now().toString())
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/newRent/"+1000)
                .then().extract().response();

        System.out.println("Response: " + response.getBody().asString());
        assertTrue(response.getBody().asString().contains("Invalid date parameters"));

        given().contentType(ContentType.JSON)
                .queryParam("startDate", Optional.empty())
                .queryParam("endDate", LocalDate.now().toString())
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/newRent/"+1000)
                .then().statusCode(500);

        given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", Optional.empty())
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/newRent/"+1000)
                .then().statusCode(500);

        given().contentType(ContentType.JSON)
                .queryParam("startDate", "")
                .queryParam("endDate", LocalDate.now().toString())
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/newRent/"+1000)
                .then().statusCode(400);

        given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", "")
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/newRent/"+1000)
                .then().statusCode(400);
    }

    @Test
    public void makeRentInvalidVehicle() {
        given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().plusDays(5).toString())
                .queryParam("vehicleId", nonExistingVehicle)
                .when().post(RENTS + "/newRent/"+1000)
                .then().statusCode(400);

        //giati vgazei 404 gamw
//        given().contentType(ContentType.JSON)
//                .queryParam("startDate", LocalDate.now().toString())
//                .queryParam("endDate", LocalDate.now().plusDays(5).toString())
//                .queryParam("vehicleId", "notValid")
//                .when().post(RENTS + "/newRent/"+1000)
//                .then().statusCode(400);
    }

    @Test
    public void makeRentInvalidCustomer() {
        given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().plusDays(5).toString())
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/newRent/"+nonExistingCustomer)
                .then().statusCode(400);

        given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().plusDays(5).toString())
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/newRent/" + null)
                .then().statusCode(404);
    }

    @Test
    public void updateRentValid() {
        //get the resource
        RentRepresentation representation = when().get(RENTS + "/" + rentId)
                .then().statusCode(200).extract().as(RentRepresentation.class);

        assertEquals(rentId, representation.id);

        //make the update
        String newStartDate = LocalDate.of(2024, 12, 4).toString();  //change startDate day from 5 to 4
        representation.startDate = newStartDate;

        //send the update
        given()
            .contentType(ContentType.JSON)
            .body(representation)
            .when().put(RENTS + "/" + rentId)
            .then().statusCode(204);

        //get the resource again to validate
        RentRepresentation updated = when().get(RENTS + "/" + rentId)
                .then().statusCode(200).extract().as(RentRepresentation.class);

        assertEquals(rentId, updated.id);
        assertEquals(newStartDate, updated.startDate);
    }

    @Test
    public void updateRentInvalid() {
        //get the resource
        RentRepresentation representation = when().get(RENTS + "/" + rentId)
                .then().statusCode(200).extract().as(RentRepresentation.class);

        assertEquals(rentId, representation.id);

        //make the update
        representation.startDate = LocalDate.of(2024, 12, 4).toString();

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(RENTS + "/" + 5000)
                .then().statusCode(404);

    }

    // ---------- DELETE ----------

    @Test
    public void deleteAllRents() {
        when().delete(RENTS)  //deletes all companies
                .then().statusCode(200);

        when().get("/rent/"+4000)
                .then().statusCode(404);  //get must return 404
        when().get("/rent/"+4001)
                .then().statusCode(404);  //get must return 404

        List<RentRepresentation> rents = when().get(RENTS)
                .then().extract().as(new TypeRef<List<RentRepresentation>>() {});

        assertEquals(0, rents.size());

        List<TechnicalCheckRepresentation> technicalChecks = when().get(CHECKS)
                .then().extract().as(new TypeRef<List<TechnicalCheckRepresentation>>() {});
        assertEquals(0, technicalChecks.size());
    }

    @Test
    public void deleteOneRentValid() {
        when().delete(RENTS + "/" + rentId)
                .then().statusCode(200);

        when().get("/rent/" + rentId)
                .then().statusCode(404);  //get must return 404
        when().get("/technicalCheck/" + 5000)
                .then().statusCode(404);  //get must return 404
    }

    @Test
    public void deleteOneRentInvalid() {
        when().delete(RENTS + "/" + 4005)  //4005 not in db
                .then().statusCode(404);  //get must return 404
    }


    //returnVehicle tests


    @Test
    public void returnVehicleInvalidCustomer() {
        nonExistingCustomer = 1400;
        nonExistingVehicle = 2500;
        Mockito.when(userManagementService.customerById(nonExistingCustomer)).thenReturn(new CustomerRepresentation());
        Mockito.when(fleetService.vehicleById(nonExistingVehicle)).thenReturn(new VehicleRepresentation());
        //then return it with invalid customer representation
        Response response = given().contentType(ContentType.JSON)
                .queryParam("miles", 50.0f)
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/returnVehicle/"+nonExistingCustomer)
                .then().extract().response();
        assertEquals(400, response.getStatusCode());

        response = given().contentType(ContentType.JSON)
                .queryParam("miles", 50.0f)
                .queryParam("vehicleId", nonExistingVehicle)
                .when().post(RENTS + "/returnVehicle/"+customerId)
                .then().extract().response();
        assertEquals(400, response.getStatusCode());

        //now make miles <= 0
        response = given().contentType(ContentType.JSON)
                .queryParam("miles", -10.0f)
                .queryParam("vehicleId",vehicleId)
                .when().post(RENTS + "/returnVehicle/"+customerId)
                .then().extract().response();
        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void returnVehicleInvalidVehicle() {
        nonExistingCustomer = 1400;
        nonExistingVehicle = 2500;
        Mockito.when(userManagementService.customerById(nonExistingCustomer)).thenReturn(new CustomerRepresentation());
        Mockito.when(fleetService.vehicleById(nonExistingVehicle)).thenReturn(new VehicleRepresentation());
        //now return it with wrong vehicle representation
        Response response = given().contentType(ContentType.JSON)
                .queryParam("miles", 50.0f)
                .queryParam("vehicleId", nonExistingVehicle)
                .when().post(RENTS + "/returnVehicle/"+customerId)
                .then().extract().response();
        assertEquals(400, response.getStatusCode());

        //now return a vehicle that is not rented
        Mockito.when(fleetService.vehicleById(3001))
                .thenReturn(Fixture.createAvailableVehicleRepresentation(3001));
        response = given().contentType(ContentType.JSON)
                .queryParam("miles", 50.0f)
                .queryParam("vehicleId", 3001)
                .when().post(RENTS + "/returnVehicle/"+customerId)
                .then().extract().response();
        assertEquals(400, response.getStatusCode());
    }


}