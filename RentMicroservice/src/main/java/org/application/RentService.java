package org.application;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;

@RequestScoped
public class RentService {
    @Inject
    FleetService fleetService;
    @Inject
    UserManagementService userManagementService;

    @Transactional
    public boolean rentedVehicleExist(Integer id){
        return fleetService.vehicleExists(id);
    }

    @Transactional
    public boolean customerExist(Integer id){
        return userManagementService.customerExists(id);
    }

    @Transactional
    public CustomerRepresentation returnCustomerWithId(Integer id) {
        return userManagementService.customerById(id);
    }

}
