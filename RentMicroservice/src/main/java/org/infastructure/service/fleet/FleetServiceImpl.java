package org.infastructure.service.fleet;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
            if (id==null){return null;}
            vehicle = fleetAPI.listVehicleById(id);
            return vehicle;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void changeVehicleState(Integer id) {
        try{
            VehicleRepresentation vehicle = fleetAPI.listVehicleById(id);
            vehicle.vehicleState = VehicleState.Rented;
            fleetAPI.update(id, vehicle);
        } catch (Exception e) {
        }
    }


}
