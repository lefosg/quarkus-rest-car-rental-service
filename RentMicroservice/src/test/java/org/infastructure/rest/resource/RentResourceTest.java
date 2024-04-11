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
import org.domain.Rents.RentRepository;
import org.infastructure.rest.representation.RentRepresentation;
import org.infastructure.rest.representation.TechnicalCheckRepresentation;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.util.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.infastructure.rest.ApiPath.Root.CHECKS;
import static org.infastructure.rest.ApiPath.Root.RENTS;
import static org.junit.jupiter.api.Assertions.*;

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
        Mockito.when(fleetService.vehicleById(vehicleId)).thenReturn(createVehicleRepresentation(vehicleId));
        Mockito.when(fleetService.vehicleById(nonExistingVehicle))
                        .thenReturn(new VehicleRepresentation());
        Mockito.when(fleetService.vehicleExists(nonExistingVehicle)).thenReturn(false);
        Mockito.when(fleetService.vehicleById(null)).thenReturn(new VehicleRepresentation());
                //.thenThrow(new BadRequestException("[!] GET /vehicle/"+"\n\tCould not find vehicle with null id "));

        Mockito.when(rentService.pay(customerId,vehicleId,amount_money,amount_damages)).thenReturn(true);
        Mockito.when(rentService.pay(customerId,nonExistingVehicle,amount_money,amount_damages)).thenReturn(false);

        Mockito.when(fleetService.changeVehicleState(vehicleId,VehicleState.Rented)).thenReturn(true);
        Mockito.when(fleetService.changeVehicleState(nonExistingVehicle,VehicleState.Available)).thenReturn(false);
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
        when().get(RENTS + "/" + null + "/vehicle")  //id null invalid
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

    //makeRent Tests
    @Test
    public void makeRent() {
        Response response = given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().plusDays(5).toString())
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/newRent/"+customerId)
                .then().extract().response();
        System.out.println("Response: " + response.getBody().asString());

        assertEquals(200, response.getStatusCode());
        assertEquals("You rented the vehicle", response.getBody().asString());

        //cannot communicate with fleet database to get the actual vehicle -> mock it
        Mockito.when(fleetService.vehicleById(vehicleId)).thenReturn(createRentedVehicleRepresentation(vehicleId));
        VehicleRepresentation v = when().get(RENTS + "/" + rentRepository.findMaxId() + "/vehicle")
                .then()
                .extract()
                .as(VehicleRepresentation.class);
        assertEquals(vehicleId, v.id);
        assertEquals(v.vehicleState, VehicleState.Rented);
    }

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
        String newStartDate = LocalDate.of(2023, 12, 4).toString();  //change startDate day from 5 to 4
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
        when().get(RENTS + "/" + 4005)  //4005 not in db
                .then().statusCode(404);  //get must return 404
    }


    //returnVehicle tests

    @Test
    public void returnVehicle() {
        //first make a rent
        CustomerRepresentation customerRepresentation = createCustomerRepresentation(1000);
        VehicleRepresentation vehicleRepresentation = createVehicleRepresentation(3000);

        Response response = given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().plusDays(5).toString())
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS + "/newRent/"+customerRepresentation.id)
                .then().extract().response();
        System.out.println("Response: " + response.getBody().asString());

        assertEquals(200, response.getStatusCode());
        assertEquals("You rented the vehicle", response.getBody().asString());

        //then return it
        //todo to ekana VehicleState.Rented giati epairne to customerRepresentation apo thn grammh 400
        // kai oxi apo th synexeia
        vehicleRepresentation.vehicleState = VehicleState.Rented;
        response = given().contentType(ContentType.JSON)
                .queryParam("miles", 50.0f)
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS+ "/returnVehicle/"+customerRepresentation.id)
                .then().extract().response();
        System.out.println(response.getBody().asString());
        assertEquals(200, response.getStatusCode());
        assertEquals("Vehicle returned", response.getBody().asString());
    }

    @Test
    public void returnVehicleInvalidCustomer() {
        //first make a rent
        CustomerRepresentation customerRepresentation = createCustomerRepresentation(1000);
        VehicleRepresentation vehicleRepresentation =createVehicleRepresentation(3000);

        Response response = given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().plusDays(5).toString())
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS + "/newRent/"+customerRepresentation.id)
                .then().extract().response();
        System.out.println("Response: " + response.getBody().asString());

        assertEquals(200, response.getStatusCode());
        assertEquals("You rented the vehicle", response.getBody().asString());

        //then return it with invalid customer representation
        customerRepresentation.id = nonExistingCustomer;
        response = given().contentType(ContentType.JSON)
                .queryParam("miles", 50.0f)
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS + "/returnVehicle/"+customerRepresentation.id)
                .then().extract().response();
        assertEquals(400, response.getStatusCode());

        customerRepresentation.id = null;
        response = given().contentType(ContentType.JSON)
                .queryParam("miles", 50.0f)
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS + "/returnVehicle/"+customerRepresentation.id)
                .then().extract().response();
        assertEquals(404, response.getStatusCode());

        //now make miles <= 0
        customerRepresentation.id = 1000;
        response = given().contentType(ContentType.JSON)
                .queryParam("miles", -10.0f)
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS + "/returnVehicle/"+customerRepresentation.id)
                .then().extract().response();
        assertEquals(400, response.getStatusCode());
    }

    @Test
    public void returnVehicleInvalidVehicle() {
        //first make a rent
        CustomerRepresentation customerRepresentation = createCustomerRepresentation(1000);
        VehicleRepresentation vehicleRepresentation = createVehicleRepresentation(3000);

        Response response = given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().plusDays(5).toString())
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS + "/newRent/"+customerRepresentation.id)
                .then().extract().response();
        assertEquals(200, response.getStatusCode());

        //now return it with wrong vehicle representation
        vehicleRepresentation.id = nonExistingVehicle;
        response = given().contentType(ContentType.JSON)
                .queryParam("miles", 50.0f)
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS + "/returnVehicle/"+customerRepresentation.id)
                .then().extract().response();
        assertEquals(400, response.getStatusCode());

        //now return a vehicle that is not rented
        Mockito.when(fleetService.vehicleById(3001))
                .thenReturn(createAvailableVehicleRepresentation(3001));
        vehicleRepresentation.id = 3001;
        response = given().contentType(ContentType.JSON)
                .queryParam("miles", 50.0f)
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS + "/returnVehicle/"+customerRepresentation.id)
                .then().extract().response();
        assertEquals(400, response.getStatusCode());
    }

    // ---------- misc ----------

    private RentRepresentation createRentRepresentation(Integer id,Integer customerId,Integer vehicleId) {
        RentRepresentation representation = new RentRepresentation();
        representation.id = id;
        representation.startDate = LocalDate.of(2023,10,10).toString();
        representation.endDate = LocalDate.of(2023,10,20).toString();
        representation.rentState = RentState.Finished;
        representation.fixedCost = new Money(770);  //assume vehicle with id 3007 is rented
        representation.miles = 130;  //company with id 2001 which owns the vehicles 3007, policy: .15 -> 100, .25 -> 200
        representation.mileageCost = new Money(45.25);
        representation.damageCost = new Money(0);  //assume no damage
        representation.totalCost = new Money(representation.mileageCost.getAmount() +
                representation.fixedCost.getAmount() + representation.damageCost.getAmount());
        representation.vehicleId = vehicleId;
        representation.customerId = customerId;
        representation.technicalCheck = 1;
        return representation;
    }

    private VehicleRepresentation createAvailableVehicleRepresentation(Integer id) {
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
        representation.companyId = 2000;
        return representation;
    }

    private VehicleRepresentation createRentedVehicleRepresentation(Integer id) {
        VehicleRepresentation representation = new VehicleRepresentation();
        representation.id = id;
        representation.manufacturer = "TOYOTA";
        representation.model = "YARIS";
        representation.year = 2015;
        representation.miles = 100000;
        representation.plateNumber = "YMB-6325";
        representation.vehicleType = VehicleType.Hatchback;
        representation.vehicleState = VehicleState.Rented;
        representation.fixedCharge = new Money(30);
        representation.companyId = 2000;
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
        representation.companyId = 2000;
        return representation;
    }

    private VehicleRepresentation createAudiVehicleRepresentation(Integer id) {
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
        return representation;
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

    private TechnicalCheckRepresentation createTechnicalCheckRepresentation(Integer id) {
        TechnicalCheckRepresentation representation = new TechnicalCheckRepresentation();
        representation.id = 5002;
        representation.damageType = DamageType.NoDamage;
        return representation;
    }
}