package org.application;

import org.infastructure.service.userManagement.representation.CustomerRepresentation;

public interface UserManagementService {
    boolean customerExists(Integer id);

    CustomerRepresentation customerById(Integer id);
}
