package org.infastructure.rest.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;

import static io.restassured.RestAssured.given;
import static org.infastructure.rest.ApiPath.Root.CHARGING_POLICY;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.domain.company.ChargingPolicy;
import org.domain.company.ChargingPolicyRepository;
import org.domain.company.Company;
import org.domain.company.CompanyRepository;
import org.infastructure.rest.representation.ChargingPolicyMapper;
import org.infastructure.rest.representation.CompanyRepresentation;
import org.junit.jupiter.api.Test;
import org.infastructure.rest.representation.ChargingPolicyRepresentation;
import org.util.Constants;
import org.util.DamageType;
import org.util.IntegrationBase;

import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ChargingPolicyResourceTest extends IntegrationBase {

    @Inject
    ChargingPolicyRepository chargingPolicyRepository;
    @Inject
    CompanyRepository companyRepository;
    @Inject
    ChargingPolicyMapper chargingPolicyMapper;

    Integer polId = 1500;

    // ---------- GET ----------

    @Test
    public void getAllPolicies() {
        List<ChargingPolicyRepresentation> policies = when().get(CHARGING_POLICY)
                .then()
                .extract()
                .as(new TypeRef<List<ChargingPolicyRepresentation>>() {});
        assertEquals(2, policies.size());
    }

    @Test
    public void getPolicyByIdValid() {
        ChargingPolicyRepresentation representation = when().get(CHARGING_POLICY + "/"+polId)
                .then()
                .extract()
                .as(ChargingPolicyRepresentation.class);
        assertEquals(polId, representation.id);
    }

    @Test
    public void getPolicyByIdInvalid() {
        when().get("/policy/"+1503)  //1503 not in db
                .then()
                .statusCode(404);
    }

    // ---------- PUT ----------

    //@Test
    public void createPolicyValid() {
        ChargingPolicyRepresentation chargingPolicy = createChargingPolicyRepresentation(1502);
        Company company = companyRepository.findCompanyById(2000);
        company.setPolicy(chargingPolicyMapper.toModel(chargingPolicy));
        ChargingPolicyRepresentation chargingPolicyRepresentation = chargingPolicyMapper.toRepresentation(chargingPolicyMapper.toModel(chargingPolicy));

        ChargingPolicyRepresentation created = given()
                .contentType(ContentType.JSON)
                .body(chargingPolicyRepresentation)
                .when().put(CHARGING_POLICY+"/createPolicy/2000")
                .then().statusCode(201)
                .header("Location", Constants.API_ROOT + CHARGING_POLICY + "/" +chargingPolicy.id)
                .extract().as(ChargingPolicyRepresentation.class);

        assertEquals(1502, created.id);
    }

    //@Test
    public void createPolicyInvalid() {
        ChargingPolicyRepresentation representation = createChargingPolicyRepresentation(1500);  //1500 in db

        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(CHARGING_POLICY+"/createPolicy")
                .then().statusCode(404);

        representation = createChargingPolicyRepresentation(null);  //null value invalid

        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(CHARGING_POLICY)
                .then().statusCode(404);
    }

    @Test
    public void updatePolicyValid() {
        //get the resource
        ChargingPolicyRepresentation representation = when().get(CHARGING_POLICY + "/"+polId)
                .then().statusCode(200).extract().as(ChargingPolicyRepresentation.class);

        assertEquals(polId, representation.id);

        //make the update
        representation.damageType.put(DamageType.Machine, 400f);

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(CHARGING_POLICY + "/"+polId)
                .then().statusCode(204);

        //get the resource again to validate
        ChargingPolicyRepresentation updated = when().get(CHARGING_POLICY + "/"+polId)
                .then().statusCode(200).extract().as(ChargingPolicyRepresentation.class);

        assertEquals(polId, updated.id);
        assertEquals(400f, updated.damageType.get(DamageType.Machine));
    }

    @Test
    public void updatePolicyInvalid() {
        //get the resource
        ChargingPolicyRepresentation representation = when().get(CHARGING_POLICY + "/"+polId)
                .then().statusCode(200).extract().as(ChargingPolicyRepresentation.class);

        assertEquals(polId, representation.id);

        //make the update
        representation.damageType.put(DamageType.Machine, 400f);

        //send the update to wrong endpoint, id mismatching
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(CHARGING_POLICY + "/1503")
                .then().statusCode(404);

        //make another update, set id to null -> should return 404
        representation.id = null;
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/policy/1500")
                .then().statusCode(404);
    }


    // ---------- misc ----------

    private ChargingPolicyRepresentation createChargingPolicyRepresentation(Integer id) {
        LinkedHashMap<Integer, Float> mileageScale = new LinkedHashMap<>();
        mileageScale.put(100, 0.13f);
        mileageScale.put(200, 0.23f);
        mileageScale.put(300, 0.33f);
        LinkedHashMap<DamageType, Float> damages = new LinkedHashMap<>();
        damages.put(DamageType.NoDamage, 0f);
        damages.put(DamageType.Glasses, 100f);
        damages.put(DamageType.Machine, 300f);
        damages.put(DamageType.Interior, 250f);
        damages.put(DamageType.Tyres, 180f);
        damages.put(DamageType.Scratches, 80f);

        ChargingPolicyRepresentation representation = new ChargingPolicyRepresentation();
        representation.id = id;
        representation.mileageScale = mileageScale;
        representation.damageType = damages;
        return representation;
    }

}