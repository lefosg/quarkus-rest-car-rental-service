package org.application;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class VehicleService {

    @Inject
    private UserManagementService userService;

    @Transactional
    public boolean companyExists(Integer id) {
        return userService.companyExists(id);
    }
}
