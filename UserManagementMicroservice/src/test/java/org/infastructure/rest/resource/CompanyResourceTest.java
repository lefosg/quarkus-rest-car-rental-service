package org.infastructure.rest.resource;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.infastructure.rest.representation.ChargingPolicyRepresentation;
import org.infastructure.rest.representation.CompanyRepresentation;
import org.junit.jupiter.api.Test;

import org.util.*;

import java.util.LinkedHashMap;
import java.util.List;

import static org.infastructure.rest.ApiPath.Root.COMPANY;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CompanyResourceTest extends IntegrationBase {

    Integer compId = 2000;
    String compCity = "ΑΘΗΝΑ";

    // ---------- GET ----------

    @Test
    public void listAllCompanies() {
        List<CompanyRepresentation> companies = when().get(COMPANY)
                .then()
                .extract()
                .as(new TypeRef<List<CompanyRepresentation>>() {});

        assertEquals(2, companies.size());
    }

    @Test
    public void listCompaniesByCityValid() {
        List<CompanyRepresentation> companies = when().get(COMPANY + "/" + "?city=ΑΘΗΝΑ")
                .then()
                .extract()
                .as(new TypeRef<List<CompanyRepresentation>>() {});

        assertEquals(1, companies.size());
    }

    @Test
    public void listCompaniesByCityInvalid() {
        List<CompanyRepresentation> companies = when().get(COMPANY+ "/" + "?city=ΚΟΖΑΝΗ")  //ΚΟΖΑΝΗ not in db
                .then()
                .extract()
                .as(new TypeRef<List<CompanyRepresentation>>() {});

        assertEquals(0, companies.size());
    }

    @Test
    public void listCompanyByIdValid() {
        CompanyRepresentation company = when().get(COMPANY + "/" + compId)
                .then()
                .extract()
                .as(CompanyRepresentation.class);

        assertEquals(2000, company.id);
    }

    @Test
    public void listCompanyByIdInvalid() {
        when().get(COMPANY + "/" + 2003)  //id 2003 not existent in db
                .then()
                .statusCode(404);
    }

//   todo mockito

//    @Test
//    public void listCompanyVehicles() {
//        List<VehicleRepresentation> vehicles = when().get("/company/" + compId + "/vehicles")
//                .then()
//                .extract()
//                .as(new TypeRef<List<VehicleRepresentation>>() {});
//
//        assertEquals(6, vehicles.size());
//    }

    @Test
    public void listCompanyPolicy() {
        ChargingPolicyRepresentation policyRepresentation = when().get(COMPANY + "/" + compId + "/policy")
                .then()
                .extract()
                .as(ChargingPolicyRepresentation.class);

        assertEquals(1500, policyRepresentation.id);
    }


    // ---------- PUT ----------

    @Test
    public void createValid() {
        CompanyRepresentation representation = createCompanyRepresentation((Integer) 2002);

        CompanyRepresentation created = given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(COMPANY)
                .then().statusCode(201).header("Location", Constants.API_ROOT+ COMPANY + "/" +representation.id)
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
            .when().put(COMPANY)
            .then().statusCode(404);
    }

    //assert that it does not create a resource with existing id
    @Test
    public void createExistentId() {
        CompanyRepresentation representation = createCompanyRepresentation((Integer) 2001);

        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(COMPANY)
                .then().statusCode(404);
    }

    @Test
    public void updateCompanyValid() {
        //get the resource
        CompanyRepresentation representation = when().get(COMPANY+ "/" +compId)
                .then().statusCode(200).extract().as(CompanyRepresentation.class);

        assertEquals(compId, representation.id);
        //make the update
        String newName = "AVIS AE";
        representation.name = newName;

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(COMPANY + "/"+compId)
                .then().statusCode(204);

        //get the resource again to validate the update
        CompanyRepresentation updated = when().get(COMPANY + "/"+compId)
                .then().statusCode(200).extract().as(CompanyRepresentation.class);

        assertEquals(compId, updated.id);
        assertEquals(newName, updated.name);
    }

    @Test
    public void updateCompanyInvalid() {
        //get the resource
        CompanyRepresentation representation = when().get(COMPANY+ "/" + compId)
                .then().statusCode(200).extract().as(CompanyRepresentation.class);

        assertEquals(compId, representation.id);
        //make the update
        representation.id = 2001;  //id 2001 in db

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(COMPANY+ "/" +compId)
                .then().statusCode(500);

        //make the update
        representation.id = null;  //null value invalid

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(COMPANY+ "/" +compId)
                .then().statusCode(500);
    }

    //@Test
    public void updateChargingPolicyValid() {
        //get the resource
        ChargingPolicyRepresentation policyRepresentation = when().get(COMPANY+ "/" + compId + "/policy")
                .then()
                .extract()
                .as(ChargingPolicyRepresentation.class);

        assertEquals(1500, policyRepresentation.id);
        //make the update
        policyRepresentation.mileageScale.put(100, 0.05f);

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(policyRepresentation)
                .when().put("/company/"+compId+"/updatePolicy")
                .then().statusCode(200);

        //get the resource again to validate the update
        ChargingPolicyRepresentation updated = when().get("/company/"+compId+"/policy")
                .then().statusCode(200).extract().as(ChargingPolicyRepresentation.class);

        assertEquals(1500, updated.id);
        assertEquals(0.05f, updated.mileageScale.get(100));
    }

    // ---------- POST ----------
//   todo mockito
//    @Test
//    public void addVehicleInvalid() {
//        VehicleRepresentation vehicleRepresentation = createVehicleRepresentation(null, compId);
//        given()
//                .contentType(ContentType.JSON)
//                .body(vehicleRepresentation)
//                .when().post("/company/"+compId+"/addVehicle")
//                .then().statusCode(404);
//
//        vehicleRepresentation = createVehicleRepresentation(3012, null);
//        given()
//                .contentType(ContentType.JSON)
//                .body(vehicleRepresentation)
//                .when().post("/company/"+compId+"/addVehicle")
//                .then().statusCode(404);
//
//        vehicleRepresentation = createVehicleRepresentation(3010, compId);  //3010 already in db
//        given()
//                .contentType(ContentType.JSON)
//                .body(vehicleRepresentation)
//                .when().post("/company/"+compId+"/addVehicle")
//                .then().statusCode(404);
//
//        vehicleRepresentation = createVehicleRepresentation(3012, 2005);  //company 2005 not in db
//        given()
//                .contentType(ContentType.JSON)
//                .body(vehicleRepresentation)
//                .when().post("/company/"+compId+"/addVehicle")
//                .then().statusCode(404);
//
//    }
//
//    @Test
//    public void addVehicleValid() {
//        VehicleRepresentation vehicleRepresentation = createVehicleRepresentation(3012, compId);
//
//        VehicleRepresentation created = given()
//                .contentType(ContentType.JSON)
//                .body(vehicleRepresentation)
//                .when().post("/company/"+compId+"/addVehicle")
//                .then().statusCode(200)
//                .extract().as(VehicleRepresentation.class);
//
//        assertEquals(1, created.id);
//        assertEquals(compId, created.company);
//
//        List<VehicleRepresentation> vehicleRepresentations = when().get("/vehicle")
//                .then().extract().as(new TypeRef<List<VehicleRepresentation>>() {});
//        assertEquals(12, vehicleRepresentations.size());
//
//        List<VehicleRepresentation> vehicles = when().get("/company/" + compId + "/vehicles")
//                .then()
//                .extract()
//                .as(new TypeRef<List<VehicleRepresentation>>() {});
//
//        assertEquals(7, vehicles.size());
//    }
//
//    // ---------- DELETE ----------
//
//    @Test
//    public void deleteAllCompanies() {
//        when().delete("/company/")  //deletes all companies
//        .then().statusCode(200);
//
//        when().get("/company/"+2000)
//                .then().statusCode(404);  //get must return 404
//        when().get("/company/"+2001)
//                .then().statusCode(404);  //get must return 404
//
//        when().get("/vehicle/"+3000)
//                .then().statusCode(404);  //getting a vehicle should return 404 as well
//
//        List<CompanyRepresentation> companies = when().get("company")
//                .then().extract().as(new TypeRef<List<CompanyRepresentation>>() {});
//        assertEquals(0, companies.size());
//
//        List<VehicleRepresentation> vehicles = when().get("vehicle")
//                .then().extract().as(new TypeRef<List<VehicleRepresentation>>() {});
//        assertEquals(0, vehicles.size());
//    }
//
//    @Test
//    public void deleteOneCompanyValid() {
//        when().delete("/company/" + compId)
//                .then().statusCode(200);
//
//        when().get("/company/" + compId)
//                .then().statusCode(404);  //get must return 404
//    }
//
//    @Test
//    public void deleteOneCompanyInvalid() {
//        when().delete("/company/" + 2005)  //2005 not in db
//                .then().statusCode(404);
//    }
//
//    // ---------- misc ----------
//
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
//
//    private VehicleRepresentation createVehicleRepresentation(Integer vehId, Integer companyId) {
//        VehicleRepresentation representation = new VehicleRepresentation();
//        representation.id = vehId;
//        representation.manufacturer = "TOYOTA";
//        representation.model = "AYGO";
//        representation.year = 2022;
//        representation.miles = 50000;
//        representation.plateNumber = "MMB-8745";
//        representation.vehicleType = VehicleType.Hatchback;
//        representation.vehicleState = VehicleState.Available;
//        representation.fixedCharge = new Money(60);
//        representation.company = companyId;
//        return representation;
//    }

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