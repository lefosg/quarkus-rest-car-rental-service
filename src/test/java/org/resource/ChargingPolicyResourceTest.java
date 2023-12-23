package org.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.representation.ChargingPolicyRepresentation;
import org.util.Constants;
import org.util.DamageType;

import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ChargingPolicyResourceTest {

    Integer polId = 1500;

    // ---------- GET ----------

    @Test
    public void getAllPolicies() {
        List<ChargingPolicyRepresentation> policies = when().get("/policy")
                .then()
                .extract()
                .as(new TypeRef<List<ChargingPolicyRepresentation>>() {});
        assertEquals(2, policies.size());
    }

    @Test
    public void getPolicyByIdValid() {
        ChargingPolicyRepresentation representation = when().get("/policy/"+polId)
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
        ChargingPolicyRepresentation representation = createChargingPolicyRepresentation(1502);

        ChargingPolicyRepresentation created = given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/policy")
                .then().statusCode(201).header("Location", Constants.API_ROOT+"/policy/"+representation.id)
                .extract().as(ChargingPolicyRepresentation.class);

        assertEquals(1502, created.id);
    }

    //@Test
    public void createPolicyInvalid() {
        ChargingPolicyRepresentation representation = createChargingPolicyRepresentation(1500);  //1500 in db

        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/policy/")
                .then().statusCode(404);

        representation = createChargingPolicyRepresentation(null);  //null value invalid

        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/policy/")
                .then().statusCode(404);
    }

    @Test
    public void updatePolicyValid() {
        //get the resource
        ChargingPolicyRepresentation representation = when().get("/policy/"+polId)
                .then().statusCode(200).extract().as(ChargingPolicyRepresentation.class);

        assertEquals(polId, representation.id);

        //make the update
        representation.damageType.put(DamageType.Machine, 400f);

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/policy/"+polId)
                .then().statusCode(204);

        //get the resource again to validate
        ChargingPolicyRepresentation updated = when().get("/policy/"+polId)
                .then().statusCode(200).extract().as(ChargingPolicyRepresentation.class);

        assertEquals(polId, updated.id);
        assertEquals(400f, updated.damageType.get(DamageType.Machine));
    }

    @Test
    public void updatePolicyInvalid() {
        //get the resource
        ChargingPolicyRepresentation representation = when().get("/policy/"+polId)
                .then().statusCode(200).extract().as(ChargingPolicyRepresentation.class);

        assertEquals(polId, representation.id);

        //make the update
        representation.damageType.put(DamageType.Machine, 400f);

        //send the update to wrong endpoint, id mismatching
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/policy/1503")
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
        LinkedHashMap<Integer, Float> mileageScale = new LinkedHashMap();
        mileageScale.put(100, 0.13f);
        mileageScale.put(200, 0.23f);
        mileageScale.put(300, 0.33f);
        LinkedHashMap<DamageType, Float> damages = new LinkedHashMap();
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