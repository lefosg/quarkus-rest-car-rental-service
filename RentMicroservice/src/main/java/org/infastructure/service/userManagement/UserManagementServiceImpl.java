package org.infastructure.service.userManagement;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.application.UserManagementService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.userManagement.UserManagementAPI;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;

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

    @Override
    public float calcMileageCosts(float miles, Integer companyId) {
        return userManagementAPI.getMileageCosts(companyId);
    }

}
