package org.resource;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import org.domain.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.representation.ChargingPolicyRepresentation;
import org.representation.CompanyMapper;
import org.representation.CompanyRepresentation;
import org.representation.VehicleRepresentation;
import org.util.IntegrationBase;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CompanyResourceTest extends IntegrationBase {

    Integer compId;
    String compCity;

    @BeforeEach
    public void setup() {
        compId = 2000;
        compCity = "ΑΘΗΝΑ";
    }

    @Test
    public void listAllCompanies() {
        List<CompanyRepresentation> companies = when().get("/company")
                .then()
                .extract()
                .as(new TypeRef<List<CompanyRepresentation>>() {});

        assertEquals(2, companies.size());
    }

    @Test
    public void listCompanyByIdValid() {
        CompanyRepresentation company = when().get("/company/" + compId)
                .then()
                .extract()
                .as(CompanyRepresentation.class);

        assertEquals(2000, company.id);
    }

    @Test
    public void listCompanyByIdInvalid() {
        when().get("/company/" + 2003)  //id 2003 not existent in db
                .then()
                .statusCode(404);
    }

    @Test
    public void listCompanyVehicles() {
        List<VehicleRepresentation> vehicles = when().get("/company/" + compId + "/vehicles")
                .then()
                .extract()
                .as(new TypeRef<List<VehicleRepresentation>>() {});

        assertEquals(6, vehicles.size());
    }

    @Test
    public void createValid() {
        CompanyRepresentation representation = new CompanyRepresentation();
        representation.id = 2002;
        representation.name = "HOLYDAYCARS";
        representation.email = "holydaycrs@gmail.com";
        representation.phone = "2218603784";
        representation.street = "ΜΑΚΕΔΟΝΙΑΣ 87";
        representation.city = "ΘΕΣΣΑΛΟΝΙΚΗ";
        representation.zipcode = "47895";
        representation.AFM = "998678010";
        representation.IBAN = "GR12564789652365";

        CompanyRepresentation created = given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/company/")
                .then().statusCode(201).header("Location","company/2002")
                .extract().as(CompanyRepresentation.class);

        assertEquals(2002, created.id);

    }

    @Test
    public void createNullId() {
        CompanyRepresentation representation = new CompanyRepresentation();
        representation.id = null;
        representation.name = "HOLYDAYCARS";
        representation.email = "holydaycrs@gmail.com";
        representation.phone = "2218603784";
        representation.street = "ΜΑΚΕΔΟΝΙΑΣ 87";
        representation.city = "ΘΕΣΣΑΛΟΝΙΚΗ";
        representation.zipcode = "47895";
        representation.AFM = "998678010";
        representation.IBAN = "GR12564789652365";

        given()
            .contentType(ContentType.JSON)
            .body(representation)
            .when().put("/company/")
            .then().statusCode(404);
    }

    @Test
    public void createExistentId() {
        CompanyRepresentation representation = new CompanyRepresentation();
        representation.id = 2001;
        representation.name = "HOLYDAYCARS";
        representation.email = "holydaycrs@gmail.com";
        representation.phone = "2218603784";
        representation.street = "ΜΑΚΕΔΟΝΙΑΣ 87";
        representation.city = "ΘΕΣΣΑΛΟΝΙΚΗ";
        representation.zipcode = "47895";
        representation.AFM = "998678010";
        representation.IBAN = "GR12564789652365";

        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/company/")
                .then().statusCode(404);
    }

}