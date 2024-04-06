package org.application;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.infastructure.service.fleet.representation.VehicleRepresentation;

public interface FleetService {

    boolean vehicleExists(Integer id);

    VehicleRepresentation vehicleById(Integer id);
}
