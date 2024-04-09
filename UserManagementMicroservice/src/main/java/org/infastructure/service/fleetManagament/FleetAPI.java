package org.infastructure.service.fleetManagament;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.infastructure.service.fleetManagament.representation.VehicleRepresentation;

import java.util.List;


@Path("/api")
@ApplicationScoped
@RegisterRestClient(configKey = "fleet-api")
public interface FleetAPI {

    @GET
    @Path("/vehicle/vehiclesByCompany/{companyId: [0-9]+}")
    List<VehicleRepresentation> getVehicles(@PathParam("companyId") Integer id);

}