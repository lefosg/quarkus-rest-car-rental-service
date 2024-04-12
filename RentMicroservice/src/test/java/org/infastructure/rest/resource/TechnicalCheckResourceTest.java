package org.infastructure.rest.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.infastructure.rest.representation.RentRepresentation;
import org.infastructure.rest.representation.TechnicalCheckRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.util.DamageType;
import org.util.IntegrationBase;
import org.util.Money;
import org.util.RentState;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.infastructure.rest.ApiPath.Root.CHECKS;
import static org.infastructure.rest.ApiPath.Root.RENTS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class TechnicalCheckResourceTest extends IntegrationBase {
    Integer technicalCheckId;
    String damageType;
    Integer rentId;

    @BeforeEach
    public void setup() {
        technicalCheckId = 5000;
        damageType = "Glasses";
        rentId = 4000;
    }
    //----GET-----
  @Test
    public void listAllTechnicalChecks() {
       List<TechnicalCheckRepresentation> technicalCheck = when().get(CHECKS)
              .then()
              .extract()
               .as(new TypeRef<List<TechnicalCheckRepresentation>>() {});

      assertEquals(2, technicalCheck.size());
 }
       @Test
       public void listTechnicalChekIdValidIdInvalid() {
           when().get(CHECKS+"/" + 3020) //id 3020 not existent
                   .then()
                   .statusCode(404);
       }

     @Test
      public void listTechnicalCheckIdValid() {
        TechnicalCheckRepresentation technicalCheck = when().get(CHECKS+"/" + technicalCheckId)
              .then()
               .extract()
              .as(TechnicalCheckRepresentation.class);

        assertEquals(technicalCheckId, technicalCheck.id);
    }
      @Test
       public void listByTechnicalCheckUnknown() {
           List<TechnicalCheckRepresentation> technicalCheck = when().get(CHECKS+"?damageType=Engine")
                  .then()
                   .extract()
                  .as(new TypeRef<List<TechnicalCheckRepresentation>>() {});

           assertEquals(0, technicalCheck.size());
      }

    //----PUT-----

    @Test
    public void updateTechnicalCheckValid() {
        //get the resource
        TechnicalCheckRepresentation representation = when().get(CHECKS +"/" + technicalCheckId)
                .then().statusCode(200).extract().as(TechnicalCheckRepresentation.class);

        assertEquals(technicalCheckId, representation.id);

        //make the update
        DamageType type = DamageType.Tyres;
        representation.damageType = type;

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put(CHECKS + "/" + technicalCheckId)
                .then().statusCode(204);

        //get the resource again to validate
        TechnicalCheckRepresentation updated = when().get(CHECKS + "/" + technicalCheckId)
                .then().statusCode(200).extract().as(TechnicalCheckRepresentation.class);
        assertEquals(technicalCheckId, updated.id);
        assertEquals(type, updated.damageType);
    }

    //----DELETE-----

    @Test
    public void deleteAllTechnicalChecks() {
        when().delete(CHECKS)
                .then().statusCode(200);

        when().get(CHECKS + "/" + 5000)
                .then().statusCode(404);  //get must return 404
        when().get(CHECKS + "/" + 5001)
                .then().statusCode(404);  //get must return 404

        List<TechnicalCheckRepresentation> representations = when().get(CHECKS)
                .then().extract().as(new TypeRef<List<TechnicalCheckRepresentation>>() {});
        assertEquals(0, representations.size());

        List<RentRepresentation> rents = when().get(RENTS)
                .then().extract().as(new TypeRef<List<RentRepresentation>>() {});
        assertEquals(2, rents.size());
    }

    @Test
    public void deleteTechnicalCheckValid() {
        when().delete(CHECKS + "/" + technicalCheckId)
                .then().statusCode(200);
        when().get(CHECKS + "/" + technicalCheckId)
                .then().statusCode(404);  //get must return 404
    }

    @Test
    public void deleteTechnicalCheckInvalid() {
        when().delete(CHECKS + "/" + 5005)  //5005 not in db
                .then().statusCode(404);
    }

}
