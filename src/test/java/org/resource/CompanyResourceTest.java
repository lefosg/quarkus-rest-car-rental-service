package org.resource;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.domain.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.representation.CompanyRepresentation;
import org.util.IntegrationBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CompanyResourceTest extends IntegrationBase {

    Integer compId;

    @BeforeEach
    public void setup() {
        compId = 2000;
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
    void listCompanyById() {
        CompanyRepresentation company = when().get("/company/" + compId)
                .then()
                .extract()
                .as(CompanyRepresentation.class);

        assertEquals(2000, company.id);
    }

}