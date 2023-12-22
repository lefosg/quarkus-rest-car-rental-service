package org.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.domain.TechnicalCheck;
import org.hibernate.metamodel.RepresentationMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.representation.CustomerRepresentation;
import org.representation.RentRepresentation;
import org.representation.TechnicalCheckRepresentation;
import org.representation.VehicleRepresentation;
import org.util.*;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

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
       List<TechnicalCheckRepresentation> technicalCheck = when().get("/technicalCheck")
              .then()
              .extract()
               .as(new TypeRef<List<TechnicalCheckRepresentation>>() {});

      assertEquals(2, technicalCheck.size());
 }
       @Test
       public void listtechnicalChekIdValidIdInvalid() {
           when().get("/technicalCheck/" + 3020) //id 3020 not existent
                   .then()
                   .statusCode(404);
       }

     @Test
      public void listTechnicalCheckIdValid() {
        TechnicalCheckRepresentation technicalCheck = when().get("/technicalCheck/" + technicalCheckId)
              .then()
               .extract()
              .as(TechnicalCheckRepresentation.class);

        assertEquals(technicalCheckId, technicalCheck.id);
    }
      @Test
       public void listByTechnicalCheckUnknown() {
           List<TechnicalCheckRepresentation> technicalCheck = when().get("/technicalCheck?damageType=Engine")
                  .then()
                   .extract()
                  .as(new TypeRef<List<TechnicalCheckRepresentation>>() {});

           assertEquals(0, technicalCheck.size());
      }
   // @Test
    public void updatetechnicalCheckValid() {
        //get the resource
        TechnicalCheckRepresentation representation = when().get("/technicalCheck/"+technicalCheckId)
                .then().statusCode(200).extract().as(TechnicalCheckRepresentation.class);

        assertEquals(technicalCheckId, representation.id);

        //make the update
        DamageType type = DamageType.Tyres;
        representation.damageType = type;

        //send the update
        given()
                .contentType(ContentType.JSON)
                .body(representation)
                .when().put("/technicalCheck/"+technicalCheckId)
                .then().statusCode(204);

        //get the resource again to validate
        TechnicalCheckRepresentation updated = when().get("/technicalCheck/"+technicalCheckId)
                .then().statusCode(200).extract().as(TechnicalCheckRepresentation.class);

        assertEquals(technicalCheckId, updated.id);
        assertEquals(type, updated.id);
    }


}
