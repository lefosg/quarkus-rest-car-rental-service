package org.infastructure.service.userManagement;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.util.DamageType;

import java.util.HashMap;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey = "userManagement-api")
public interface UserManagementAPI {

    @GET
    @Path("/customer/{customerId: [0-9]+}")
    CustomerRepresentation listCustomerById(@PathParam("customerId") Integer id);

    @GET
    @Path("/company/{companyId}: [0-9]+}/calculateCosts")
    public HashMap<String, Float> getMileageCosts(
            @QueryParam("miles") float miles,
            @QueryParam("damageType") DamageType damageType,
            @PathParam("companyId") Integer id);

    @POST
    @Path("/company/{companyId}: [0-9]+}/pay")
    public Response pay(@PathParam("companyId") Integer id,
                        @QueryParam("amount_money") float amount_money,
                        @QueryParam("amount_damages") float amount_damages);
}
