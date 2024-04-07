package org.infastructure.service.fleet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.infastructure.service.fleet.representation.VehicleRepresentation;

@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey = "fleet-api")
public interface FleetAPI {

    @GET
    @Path("/vehicle/{vehicleId: [0-9]+}")
    VehicleRepresentation listVehicleById(@PathParam("vehicleId") Integer id);

    @PUT
    @Path("/vehicle/{vehicleId: [0-9]+}")
    boolean update(@PathParam("vehicleId") Integer id, VehicleRepresentation vehicleRepresentation);
}
