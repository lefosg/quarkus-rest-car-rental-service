package org.infastructure.service.user_management;

import jakarta.enterprise.context.ApplicationScoped;
import org.application.UserManagementService;

@ApplicationScoped
public class UserManagementServiceImpl implements UserManagementService {

    @Override
    public boolean searchCompany(Integer id) {
        return false;
    }
}
