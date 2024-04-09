package org.application;

import org.infastructure.service.fleet.representation.VehicleRepresentation;

public interface FleetService {

    boolean vehicleExists(Integer id);

    VehicleRepresentation vehicleById(Integer id);

    void changeVehicleState(Integer id);

}
