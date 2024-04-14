package org.application;

import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.util.VehicleState;

public interface FleetService {

    boolean vehicleExists(Integer id);

    VehicleRepresentation vehicleById(Integer id);

    boolean changeVehicleInfo(Integer id, VehicleRepresentation vehicle);

}
