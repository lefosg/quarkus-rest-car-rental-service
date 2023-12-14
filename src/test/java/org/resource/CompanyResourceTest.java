package org.resource;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CompanyResourceTest extends IntegrationBase {

    @Test
    void listAllCompanies() {
        String a = when().get("/company")
                .then()
                .extract().asString();

        assertEquals("A", a);
    }

}