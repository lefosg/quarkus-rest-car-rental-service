package org.infastructure.service.userManagement;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.ws.rs.core.Response;
import org.application.UserManagementService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.userManagement.UserManagementAPI;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.util.DamageType;

import java.util.HashMap;

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
            if (id == null)
                return new CustomerRepresentation();
            CustomerRepresentation customer = userManagementAPI.listCustomerById(id);
            return customer;
        } catch (Exception e) {
            return new CustomerRepresentation();
        }
    }

    @Override
    public HashMap<String, Float> getAllCosts(float miles, DamageType damageType, Integer companyId) {
        return userManagementAPI.getAllCosts(miles, damageType, companyId);
    }

    @Override
    public boolean pay(Integer customerId, Integer companyId, double amount_money, double amount_damages) {
        Response response = userManagementAPI.pay(customerId, companyId, amount_money, amount_damages);
        return response.getStatus() == 200;
    }

}
