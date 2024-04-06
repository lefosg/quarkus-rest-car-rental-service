package org.infastructure.service.fleet;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.application.FleetService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;

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
            vehicle.id = fleetAPI.listVehicleById(id).id;
            vehicle.companyId = fleetAPI.listVehicleById(id).companyId;
            vehicle.vehicleState = fleetAPI.listVehicleById(id).vehicleState;
            vehicle.fixedCharge = fleetAPI.listVehicleById(id).fixedCharge;
            vehicle.manufacturer = fleetAPI.listVehicleById(id).manufacturer;
            vehicle.miles = fleetAPI.listVehicleById(id).miles;
            vehicle.model = fleetAPI.listVehicleById(id).model;
            vehicle.plateNumber = fleetAPI.listVehicleById(id).plateNumber;
            vehicle.year = fleetAPI.listVehicleById(id).year;
            vehicle.vehicleType = fleetAPI.listVehicleById(id).vehicleType;
            return vehicle;
        } catch (Exception e) {
            return null;
        }
    }

}
