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
    @Path("/company/{companyId: [0-9]+}/calculateCosts")
    public HashMap<String, Float> getAllCosts(
            @QueryParam("miles") float miles,
            @QueryParam("damageType") DamageType damageType,
            @PathParam("companyId") Integer id);

    @POST
    @Path("/customer/pay/{customerId: [0-9]+}/")
    public Response pay(@PathParam("customerId") Integer customerId,
                        @QueryParam("companyId") Integer companyId,
                        @QueryParam("amount_money") double amount_money,
                        @QueryParam("amount_damages") double amount_damages);
}
