package org.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class CustomerResourceTest extends IntegrationBase {

    @Test
    void listAllCustomers() {
        String p = when().get("/customer")
               .then()
                .extract().asString();

       assertEquals("PELATES", p);
    }
}
