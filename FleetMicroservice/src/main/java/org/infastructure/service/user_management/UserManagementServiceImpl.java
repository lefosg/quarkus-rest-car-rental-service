package org.infastructure.service.user_management;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.application.UserManagementService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.infastructure.service.user_management.representation.CompanyRepresentation;

@ApplicationScoped
public class UserManagementServiceImpl implements UserManagementService {

    @Inject
    @RestClient
    UserManagementApi userApi;

    @Override
    public boolean companyExists(Integer id) {
        try {
            CompanyRepresentation company = userApi.getCompany(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public CompanyRepresentation getCompany(Integer id) {
        try {
            CompanyRepresentation company = userApi.getCompany(id);
            return company;
        } catch (Exception e) {
            return new CompanyRepresentation();
        }
    }


}
