package org.infastructure.service.fleetManagament;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.application.FleetService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.fleetManagament.representation.VehicleRepresentation;

import java.util.List;

@ApplicationScoped
public class FleetServiceImpl implements FleetService {

    @Inject
    @RestClient
    FleetAPI fleetAPI;

    @Override
    public List<VehicleRepresentation> getFleet(Integer companyId) {
        try {
            return fleetAPI.getVehicles(companyId);
        } catch (Exception e) {
            return null;
        }
    }
}
