package org.application;

import org.infastructure.service.user_management.representation.CompanyRepresentation;

public interface UserManagementService {

    boolean companyExists(Integer id);

    CompanyRepresentation getCompany(Integer id);
}
