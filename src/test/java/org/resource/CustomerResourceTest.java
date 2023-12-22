package org.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;
import org.representation.CompanyRepresentation;
import org.representation.CustomerRepresentation;
import org.util.IntegrationBase;

import java.util.List;

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
    public void deleteOneCompany() {
        when().delete("/customer/" + custId)
            .then().statusCode(200);
        when().get("/customer/" + custId)
            .then().statusCode(404);
        when().get("/customer/" + 1005)  //1005 not in db
            .then().statusCode(404);
    }

}
