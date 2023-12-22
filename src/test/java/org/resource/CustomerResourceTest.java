package org.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.representation.CompanyRepresentation;
import org.representation.CustomerRepresentation;
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
