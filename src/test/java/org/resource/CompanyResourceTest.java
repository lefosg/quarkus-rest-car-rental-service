package org.resource;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import jakarta.ws.rs.NotFoundException;
import org.domain.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.representation.CompanyRepresentation;
import org.representation.VehicleRepresentation;
import org.util.IntegrationBase;

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
    void listAllCompanies() {
        List<CompanyRepresentation> companies = when().get("/company")
                .then()
                .extract()
                .as(new TypeRef<List<CompanyRepresentation>>() {});

        assertEquals(2, companies.size());
    }

    @Test
    void listCompanyByIdValid() {
        CompanyRepresentation company = when().get("/company/" + compId)
                .then()
                .extract()
                .as(CompanyRepresentation.class);

        assertEquals(2000, company.id);
    }

    @Test
    void listCompanyByIdInvalid() {
        when().get("/company/" + 2003)  //id 2003 not existent in db
                .then()
                .statusCode(404);
    }

//    @Test  //fixme /company/city
//    void listCompaniesByCityValid() {
//        List<CompanyRepresentation> companies = when().get("/company/" + compCity)
//                .then()
//                .extract()
//                .as(new TypeRef<List<CompanyRepresentation>>() {});
//
//        assertEquals(1, companies.size());
//    }

//    @Test
//    void listCompaniesByCityInvalid() {
//        when().get("/company/" + "ΚΟΖΑΝΗ")  //city ΚΟΖΑΝΗ not existent in db
//                .then()
//                .statusCode(404);
//    }

    @Test
    void listCompanyVehicles() {
        List<VehicleRepresentation> vehicles = when().get("/company/" + compId + "/vehicles")
                .then()
                .extract()
                .as(new TypeRef<List<VehicleRepresentation>>() {});

        assertEquals(6, vehicles.size());
    }


}