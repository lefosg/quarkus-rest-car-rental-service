package org.application;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.domain.Rents.Rent;
import org.domain.Rents.RentRepository;
import org.infastructure.rest.representation.RentMapper;
import org.infastructure.rest.representation.RentRepresentation;
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
    public void makeVehicleRented(Integer id){fleetService.changeVehicleState(id);}

    @Transactional
    public HashMap<String, Float> calculateCosts(Integer customerId, Integer vehicleId, float miles){
        Rent rent = findRent(customerId, vehicleId);
        DamageType damageType = rent.calculateDamageCost();  //todo technical check

        HashMap<String, Float> costs = calculateAllCosts(miles, damageType, rent.getVehicleId());/* + calculateDamageCost(rent) + calculateFixedCost(rent) + calculateTotalCost(rent);*/
        return costs;
    }

    @Transactional
    public void pay(Integer vehicleId, float amount_money, float amount_damages) {
        Integer companyId = fleetService.vehicleById(vehicleId).companyId;
        userManagementService.pay(companyId, amount_money, amount_damages);
    }

    private HashMap<String, Float> calculateAllCosts(float miles, DamageType damageType, Integer vehicleId){
        Integer companyId = fleetService.vehicleById(vehicleId).companyId;
        return userManagementService.calcMileageCosts(miles, damageType, companyId);
    }

    public Rent findRent(Integer customerId, Integer vehicleId) {
        ArrayList<Rent> rents = (ArrayList<Rent>) rentRepository.findRentByCustomerAndVehicle(customerId, vehicleId);
        if (rents.isEmpty()) {
            return null;
        }
        return rents.get(rents.size()-1);
    }
}
