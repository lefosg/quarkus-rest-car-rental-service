package org.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.infastructure.service.fleetManagament.representation.VehicleRepresentation;

import java.util.List;

@ApplicationScoped
public class UserService {

    @Inject
    FleetService fleetService;

    @Transactional
    public List<VehicleRepresentation> getFleet(Integer companyId){
        return fleetService.getFleet(companyId);
    };
}
