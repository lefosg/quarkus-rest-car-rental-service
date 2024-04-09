package org.application;

import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.util.DamageType;

import java.util.HashMap;

public interface UserManagementService {

    boolean customerExists(Integer id);

    CustomerRepresentation customerById(Integer id);

    HashMap<String, Float> calcMileageCosts(float miles, DamageType damageType, Integer companyId);

    boolean pay(Integer companyId, float amount_money, float amount_damages);

}
