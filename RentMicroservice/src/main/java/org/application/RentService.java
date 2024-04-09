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
    public float calculateCosts(Integer id, float miles){
        float costs = 0;
        Rent rent;
        try {
            rent = rentRepository.findRentById(id);
        } catch (Exception e){
            return 0;//todo edo na valo exception
        }
        RentRepresentation rentRepresentation = rentMapper.toRepresentation(rent);


        costs = calculateMilageCosts(rent.getMiles(),rent.getVehicleId());/* + calculateDamageCost(rent) + calculateFixedCost(rent) + calculateTotalCost(rent);*/
        return costs;
    }

    private float calculateMilageCosts(float miles,Integer vehicleId){
        Integer companyId = fleetService.vehicleById(vehicleId).companyId;
        return userManagementService.calcMileageCosts(miles,companyId);
    }
}
