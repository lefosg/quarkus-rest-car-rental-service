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
import org.util.Constants;
import org.util.IntegrationBase;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CompanyResourceTest extends IntegrationBase {

    Integer compId = (Integer) 2000;
    String compCity = "ΑΘΗΝΑ";

    // ---------- GET ----------

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

    // ---------- PUT ----------

    @Test
    public void createValid() {
        CompanyRepresentation representation = createCompanyRepresentation((Integer) 2002);

        CompanyRepresentation created = given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/company/")
                .then().statusCode(201).header("Location", Constants.API_ROOT+"/company/"+representation.id)
                .extract().as(CompanyRepresentation.class);

        assertEquals(2002, created.id);

    }

    //assert that it does not create a resource with null id
    @Test
    public void createNullId() {
        CompanyRepresentation representation = createCompanyRepresentation(null);

        given()
            .contentType(ContentType.JSON)
            .body(representation)
            .when().put("/company/")
            .then().statusCode(404);
    }

    //assert that it does not create a resource with existing id
    @Test
    public void createExistentId() {
        CompanyRepresentation representation = createCompanyRepresentation((Integer) 2001);

        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/company/")
                .then().statusCode(404);
    }

    //todo create tests for updating a resource
    @Test
    public void updateValid() {
        //get the resource
        CompanyRepresentation representation = when().get("/company/"+compId)
                .then().statusCode(200).extract().as(CompanyRepresentation.class);

        assertEquals(compId, representation.id);
        //make the update
        String newName = "AVIS AE";
        representation.name = newName;

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/company/"+compId)
                .then().statusCode(204);

        //get the resource again to validate the update
        CompanyRepresentation updated = when().get("/company/"+compId)
                .then().statusCode(200).extract().as(CompanyRepresentation.class);

        assertEquals(compId, updated.id);
        assertEquals(newName, updated.name);
    }

    // ---------- DELETE ----------

    //@Test
    public void deleteAllCompanies() {
        when().delete("/company/")
        .then().statusCode(200);
    }

    //@Test
    public void deleteOneCompany() {
        when().delete("/company/" + 2000)  //id 3012 not in db
                .then().statusCode(200);
    }


    // ---------- misc ----------

    private CompanyRepresentation createCompanyRepresentation(Integer id) {
        CompanyRepresentation representation = new CompanyRepresentation();
        representation.id = id;
        representation.name = "HOLYDAYCARS";
        representation.email = "holydaycrs@gmail.com";
        representation.phone = "2218603784";
        representation.street = "ΜΑΚΕΔΟΝΙΑΣ 87";
        representation.city = "ΘΕΣΣΑΛΟΝΙΚΗ";
        representation.zipcode = "47895";
        representation.password = "topcars123";
        representation.AFM = "998678010";
        representation.IBAN = "GR12564789652365";
        return representation;
    }
}