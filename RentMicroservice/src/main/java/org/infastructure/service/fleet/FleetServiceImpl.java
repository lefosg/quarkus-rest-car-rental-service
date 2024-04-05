package org.infastructure.service.fleet;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.application.FleetService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.fleet.representation.VehicleRepresentation;

@ApplicationScoped
public class FleetServiceImpl implements FleetService {

    @Inject
    @RestClient
    FleetAPI fleetAPI;

    @Override
    public boolean vehicleExists(Integer id) {
        try{
            VehicleRepresentation vehicle = fleetAPI.listVehicleById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
