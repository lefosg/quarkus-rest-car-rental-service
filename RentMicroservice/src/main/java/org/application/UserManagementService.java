package org.application;

import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.util.DamageType;

import java.util.HashMap;

public interface UserManagementService {

    boolean customerExists(Integer id);

    CustomerRepresentation customerById(Integer id);

    HashMap<String, Float> getAllCosts(float miles, DamageType damageType, Integer companyId);

    boolean pay(Integer customerId, Integer companyId, double amount_money, double amount_damages);

}
