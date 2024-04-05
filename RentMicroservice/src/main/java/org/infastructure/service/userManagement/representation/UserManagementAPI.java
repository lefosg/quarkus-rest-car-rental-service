package org.infastructure.service.userManagement.representation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.infastructure.service.fleet.representation.VehicleRepresentation;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey = "userManagement-api")
public interface UserManagementAPI {

    @GET
    @Path("/customer/{customerId: [0-9]+}")
    CustomerRepresentation listCustomerById(@PathParam("customerId") Integer id);
}
