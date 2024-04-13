package org.infastructure.rest.resource;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.infastructure.rest.ApiPath.Root.RENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.application.FleetService;
import org.application.RentService;
import org.application.UserManagementService;
import org.common.BusinessRuleException;
import org.domain.Rents.Rent;
import org.domain.Rents.RentRepository;
import org.glassfish.jaxb.runtime.v2.runtime.reflect.opt.Const;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.util.*;

import java.time.LocalDate;
import java.util.HashMap;


@QuarkusTest
public class RentResourceBigTests extends IntegrationBase {

    @Inject
    private RentService rentService;

    @Inject
    private RentRepository rentRepository;

    @InjectMock
    private UserManagementService userManagementService;

    @InjectMock
    private FleetService fleetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//todo lefteri voitha mia trexei mia den trexei
    @Test
    public void testReturnVehicle() {
        int customerId = 1000;
        int vehicleId = 3000;
        float miles = 100;

        CustomerRepresentation customerRepresentation = Fixture.createCustomerRepresentation(customerId);

        Mockito.when(userManagementService.customerById(customerId)).thenReturn(customerRepresentation);

        VehicleRepresentation vehicleRepresentation = Fixture.createVehicleRepresentation(vehicleId);
        vehicleRepresentation.vehicleState = VehicleState.Rented;
        vehicleRepresentation.countOfRents = 1;
        Mockito.when(fleetService.vehicleById(vehicleId)).thenReturn(vehicleRepresentation);

        Rent rent = rentService.findRent(customerId, vehicleId);

        HashMap<String, Float> costs = new HashMap<String,Float>();
        costs.put(Constants.damageCost, 50f);
        costs.put(Constants.mileageCost, 20f);
        Mockito.when(rentService.calculateCosts(customerId, vehicleId, miles)).thenReturn(costs);
        costs.put(Constants.fixedCost, 180f);
        float sum = costs.get(Constants.damageCost)+costs.get(Constants.mileageCost)+costs.get(Constants.fixedCost);
        System.out.println(costs.get(Constants.damageCost));
        System.out.println(costs.get(Constants.mileageCost));
        System.out.println(costs.get(Constants.fixedCost));
        System.out.println(sum);
        Mockito.when(userManagementService.pay(customerId, vehicleRepresentation.companyId, sum, costs.get(Constants.damageCost))).thenReturn(true);

        Response response = given().contentType(ContentType.JSON)
                .queryParam("miles", miles)
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when().post(RENTS + "/returnVehicle/"+customerRepresentation.id)
                .then().extract().response();
        assertEquals(200, response.getStatusCode());
        assertEquals("Vehicle returned", response.getBody().asString());
    }

    @Test
    public void testReturnVehicleNotPayed() {
        int customerId = 1000;
        int vehicleId = 3000;
        float miles = 100;

        CustomerRepresentation customerRepresentation = Fixture.createCustomerRepresentation(customerId);

        Mockito.when(userManagementService.customerById(customerId)).thenReturn(customerRepresentation);

        VehicleRepresentation vehicleRepresentation = Fixture.createAudiVehicleRepresentation(vehicleId);
        vehicleRepresentation.vehicleState = VehicleState.Rented;
        vehicleRepresentation.countOfRents = 1;
        Mockito.when(fleetService.vehicleById(vehicleId)).thenReturn(vehicleRepresentation);

        Rent rent = rentService.findRent(customerId, vehicleId);

        HashMap<String, Float> costs = new HashMap<>();
        costs.put(Constants.damageCost, 50f);
        costs.put(Constants.mileageCost, 20f);
        costs.put(Constants.fixedCost, 200f);
        Mockito.when(userManagementService.getAllCosts(miles,DamageType.NoDamage, vehicleRepresentation.companyId)).thenReturn(costs);

        Mockito.when(userManagementService.pay(customerId, vehicleRepresentation.companyId, 490, 50f)).thenReturn(false);
        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("miles", miles)
                .queryParam("vehicleId", vehicleRepresentation.id)
                .when()
                .post(RENTS + "/returnVehicle/" + customerRepresentation.id)
                .then()
                .extract()
                .response();
        assertEquals(500, response.getStatusCode());

    }

    @Test
    public void makeRent() {
        int customerId = 1001;
        int vehicleId = 3001;
        Mockito.when(userManagementService.customerById(customerId)).thenReturn(Fixture.createCustomerRepresentation(customerId));
        VehicleRepresentation vehicleRepresentation = Fixture.createRentedVehicleRepresentation(vehicleId);
        vehicleRepresentation.vehicleState = VehicleState.Available;
        Mockito.when(fleetService.vehicleById(vehicleId)).thenReturn(vehicleRepresentation);

        Response response = given().contentType(ContentType.JSON)
                .queryParam("startDate", LocalDate.now().toString())
                .queryParam("endDate", LocalDate.now().plusDays(5).toString())
                .queryParam("vehicleId", vehicleId)
                .when().post(RENTS + "/newRent/"+customerId)
                .then().extract().response();
        vehicleRepresentation.vehicleState = VehicleState.Rented;

        assertEquals(200, response.getStatusCode());
        assertEquals("You rented the vehicle", response.getBody().asString());

        //cannot communicate with fleet database to get the actual vehicle -> mock it
        VehicleRepresentation v = when().get(RENTS + "/" + rentRepository.findMaxId() + "/vehicle")
                .then()
                .extract()
                .as(VehicleRepresentation.class);
        assertEquals(vehicleId, v.id);
        assertEquals(v.vehicleState, VehicleState.Rented);
    }


}