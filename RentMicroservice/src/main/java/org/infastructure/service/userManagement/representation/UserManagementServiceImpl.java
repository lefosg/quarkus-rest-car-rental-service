package org.infastructure.service.userManagement.representation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.application.UserManagementService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.fleet.representation.VehicleRepresentation;

@ApplicationScoped
public class UserManagementServiceImpl implements UserManagementService {

    @Inject
    @RestClient
    UserManagementAPI userManagementAPI;

    @Override
    public boolean customerExists(Integer id){
        try{
            CustomerRepresentation customer = userManagementAPI.listCustomerById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public CustomerRepresentation customerById(Integer id) {
        try{
            CustomerRepresentation customer =  new CustomerRepresentation();
            customer = userManagementAPI.listCustomerById(id);
            return customer;
        } catch (Exception e) {
            return null;
        }
    }
}
