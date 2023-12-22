package org.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.domain.Customer;
import org.junit.jupiter.api.Test;
import org.representation.CompanyRepresentation;
import org.representation.CustomerRepresentation;
import org.util.Constants;
import org.util.IntegrationBase;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class CustomerResourceTest extends IntegrationBase {

    Integer custId = 1000;

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
    public void createInvalidId() {
        CustomerRepresentation representation = createCustomerRepresentation(1000);  //id 1000 in db

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
        representation.name = "ΔΗΜΗΤΡΑ";
        representation.email = "dimitra@yahoo.com";
        representation.password = "dimitra";
        representation.phone = "6912483189";
        representation.street = "ΚΡΥΣΤΑΛΛΗ 52";
        representation.city = "ΑΘΗΝΑ";
        representation.zipcode = "16478";
        representation.AFM = "199371245";
        representation.surname = "ΚΥΠΡΑΙΟΥ";
        representation.expirationDate = LocalDate.of(2029,10,15).toString();
        representation.number = "15482634678126";
        representation.holderName = "ΔΗΜΗΤΡΑ ΚΥΠΡΑΙΟΥ";
        return representation;
    }

}
