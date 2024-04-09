package org.application;

import org.infastructure.service.fleetManagament.representation.VehicleRepresentation;

import java.util.List;

public interface FleetService {

    public List<VehicleRepresentation> getFleet(Integer companyId);
}
