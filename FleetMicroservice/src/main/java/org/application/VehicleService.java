package org.application;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.infastructure.service.user_management.representation.CompanyRepresentation;

@RequestScoped
public class VehicleService {

    @Inject
    private UserManagementService userService;

    @Transactional
    public boolean companyExists(Integer id) {
        return userService.companyExists(id);
    }

    @Transactional
    public CompanyRepresentation getCompany(Integer id) { return userService.getCompany(id); }

}
