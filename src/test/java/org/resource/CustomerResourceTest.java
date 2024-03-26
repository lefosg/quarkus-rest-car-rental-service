package org.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.domain.Customer;
import org.junit.jupiter.api.Test;
import org.representation.CompanyRepresentation;
import org.representation.CustomerRepresentation;
import org.representation.RentRepresentation;
import org.representation.VehicleRepresentation;
import org.util.*;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class CustomerResourceTest extends IntegrationBase {

    Integer custId = 1000;
    Integer compId = 2000;

    // ---------- GET ----------

    @Test
    public void listAllCustomers() {
        List<CustomerRepresentation> representations = when().get("/customer")
               .then()
                .extract().as(new TypeRef<List<CustomerRepresentation>>() {});

       assertEquals(2, representations.size());
    }
    @Test
    public void listCustomerByIdValid() {
        CustomerRepresentation customer = when().get("/customer/" + custId)
                .then()
                .extract()
                .as(CustomerRepresentation.class);

        assertEquals(custId, customer.id);
    }

    @Test
    public void listCustomerByIdInvalid() {
        when().get("/customer/" + 2003)  //id 2003 not existent in db
                .then()
                .statusCode(404);
    }

    // ---------- PUT ----------
    @Test
    public void createNullId() {
        CustomerRepresentation representation = createCustomerRepresentation(null);

        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/customer/")
                .then().statusCode(404);
    }

    @Test
    public void createValid() {
        CustomerRepresentation representation = createCustomerRepresentation((Integer) 55);

        CustomerRepresentation created = given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/customer/")
                .then().statusCode(201).header("Location", Constants.API_ROOT+"/customer/"+representation.id)
                .extract().as(CustomerRepresentation.class);

        assertEquals(55, created.id);

    }

    @Test
    public void updateCustomerValid() {
        //get the resource
        CustomerRepresentation representation = when().get("/customer/"+custId)
                .then().statusCode(200).extract().as(CustomerRepresentation.class);

        assertEquals(custId, representation.id);
        //make the update
        String newName = "kwstas mhtsos";
        representation.name = newName;

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/customer/"+custId)
                .then().statusCode(204);

        //get the resource again to validate the update
        CustomerRepresentation updated = when().get("/customer/"+custId)
                .then().statusCode(200).extract().as(CustomerRepresentation.class);

        assertEquals(custId, updated.id);
        assertEquals(newName, updated.name);
    }

    // ---------- DELETE ----------

    @Test
    public void deleteAllCustomers() {
        when().delete("/customer/")
            .then().statusCode(200);

        when().get("/customer/"+1000)
            .then().statusCode(404);
        when().get("/customer/"+1001)
            .then().statusCode(404);
    }

    @Test
    public void deleteOneCustomer() {
        when().delete("/customer/" + custId)
            .then().statusCode(200);
        when().get("/customer/" + custId)
            .then().statusCode(404);
        when().get("/customer/" + 1005)  //1005 not in db
            .then().statusCode(404);
    }
    private CustomerRepresentation createCustomerRepresentation(Integer id) {
        CustomerRepresentation representation = new CustomerRepresentation();
        representation.id = id;
        representation.name = "ΙΩΑΝΝΗS";
        representation.email = "evangellKu@gmail.com";
        representation.password = "johnjohD";
        representation.phone = "6941603678";
        representation.street = "ΛΕΥΚΑΔΟΣ 25";
        representation.city = "ΑΘΗΝC";
        representation.zipcode = "35895";
        representation.AFM = "166008285";
        representation.surname = "ΕΥΑΓΓΕΛoΥ";
        representation.expirationDate = LocalDate.of(2027,11,26).toString();
        representation.number = "7894665213797564";
        representation.holderName = "ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛoΥ";
        return representation;
    }

    private VehicleRepresentation createVehicleRepresentation(Integer vehId, Integer companyId) {
        VehicleRepresentation representation = new VehicleRepresentation();
        representation.id = vehId;
        representation.manufacturer = "TOYOTA";
        representation.model = "AYGO";
        representation.year = 2022;
        representation.miles = 50000;
        representation.plateNumber = "MMB-8745";
        representation.vehicleType = VehicleType.Hatchback;
        representation.vehicleState = VehicleState.Available;
        representation.fixedCharge = new Money(60);
        representation.company = companyId;
        return representation;
    }

}
