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
import org.domain.Vehicle;
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
public class VehicleResourceTest extends IntegrationBase {

  // @BeforeEach
 //  public void setup() {
       //manufacturer = "TOYOTA";
       // model  = "YARIS";
 //   }

    @Test
    public void listAllVehicles() {
        List<VehicleRepresentation> vehicles = when().get("/vehicle")
                .then()
                .extract()
                .as(new TypeRef<List<VehicleRepresentation>>() {});

        assertEquals(11, vehicles.size());
    }

  //  @Test
  //  public void listVehicleByModelValid() {
   //     VehicleRepresentation vehicle = when().get("/vehicle/" + model)
       //         .then()
         //       .extract()
        //        .as(VehicleRepresentation.class);

      //  assertEquals("YARIS", vehicle.model);
  //  }
}
