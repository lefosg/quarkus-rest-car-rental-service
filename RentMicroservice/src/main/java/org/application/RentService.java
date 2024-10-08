package org.application;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.domain.Rents.Rent;
import org.domain.Rents.RentRepository;
import org.infastructure.rest.representation.RentMapper;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.util.DamageType;

import java.util.ArrayList;
import java.util.HashMap;

@RequestScoped
public class RentService {
    @Inject
    FleetService fleetService;
    @Inject
    UserManagementService userManagementService;
    @Inject
    RentRepository rentRepository;
    @Inject
    RentMapper rentMapper;
    @Inject
    TechnicalCheckService technicalCheckService;

    @Transactional
    public boolean rentedVehicleExist(Integer id){
        return fleetService.vehicleExists(id);
    }

    @Transactional
    public boolean customerExist(Integer id){
        return userManagementService.customerExists(id);
    }

    @Transactional
    public CustomerRepresentation returnCustomerWithId(Integer id) {
        return userManagementService.customerById(id);
    }

    @Transactional
    public VehicleRepresentation returnVehicleWithId(Integer id) {
        return fleetService.vehicleById(id);
    }

    @Transactional
    public void updateVehicle(Integer id, VehicleRepresentation vehicle){
        fleetService.changeVehicleInfo(id,vehicle);
    }

    @Transactional
    public HashMap<String, Float> calculateCosts(Integer customerId, Integer vehicleId, float miles){
        Rent rent = findRent(customerId, vehicleId);
        DamageType damageType = doTechnicalCheck(vehicleId, rent.getTechnicalCheck().getId());
        if (damageType == null)
            return null;

        HashMap<String, Float> costs = calculateAllCosts(miles, damageType, rent.getVehicleId());/* + calculateDamageCost(rent) + calculateFixedCost(rent) + calculateTotalCost(rent);*/
        return costs;
    }

    private DamageType doTechnicalCheck(Integer vehicleId, Integer technicalCheckId) {
        DamageType damageType = technicalCheckService.doTechnicalCheck(vehicleId, technicalCheckId);
        if (damageType == null) {
            return null;
        }
        return damageType;
    }

    private HashMap<String, Float> calculateAllCosts(float miles, DamageType damageType, Integer vehicleId){
        Integer companyId = fleetService.vehicleById(vehicleId).companyId;
        return userManagementService.getAllCosts(miles, damageType, companyId);
    }

    public Rent findRent(Integer customerId, Integer vehicleId) {
        ArrayList<Rent> rents = (ArrayList<Rent>) rentRepository.findRentByCustomerAndVehicle(customerId, vehicleId);
        if (rents.isEmpty()) {
            return null;
        }
        return rents.get(rents.size()-1);
    }

    @Transactional
    public boolean pay(Integer customerId, Integer vehicleId, double amount_money, double amount_damages) {
        VehicleRepresentation vehicleRepresentation = fleetService.vehicleById(vehicleId);
        Integer companyId = null;
        if(fleetService.vehicleById(vehicleId)==null){
            return false;
        }
        companyId = vehicleRepresentation.companyId;
        System.out.println(amount_damages);
        System.out.println(amount_money);
        return userManagementService.pay(customerId, companyId, amount_money, amount_damages);
    }
}
