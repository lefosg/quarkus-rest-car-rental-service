package org.infastructure.service.fleet;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.application.FleetService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.util.VehicleState;

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

    @Override
    public VehicleRepresentation vehicleById(Integer id) {
        try{
            VehicleRepresentation vehicle = new VehicleRepresentation();
            if (id==null){return new VehicleRepresentation();}
            vehicle = fleetAPI.listVehicleById(id);
            return vehicle;
        } catch (Exception e) {
            return new VehicleRepresentation();
        }
    }

    @Override
    public boolean changeVehicleState(Integer id,VehicleState vehicleState) {
        try{
            VehicleRepresentation vehicle = fleetAPI.listVehicleById(id);
            vehicle.vehicleState = vehicleState;
            try {
                Response response = fleetAPI.update(id, vehicle);
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
