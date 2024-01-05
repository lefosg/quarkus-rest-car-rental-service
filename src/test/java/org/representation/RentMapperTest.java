package org.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.domain.Customer;
import org.domain.Rent;
import org.domain.Vehicle;
import org.junit.jupiter.api.Test;
import org.persistence.CustomerRepository;
import org.persistence.RentRepository;
import org.persistence.VehicleRepository;
import org.util.IntegrationBase;
import org.util.Money;
import org.util.RentState;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RentMapperTest extends IntegrationBase {

    @Inject
    RentMapper rentMapper;

    @Inject
    RentRepository rentRepository;

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    CustomerRepository customerRepository;


    @Test
    public void testToModel() {

        RentRepresentation representation = createRentRepresentation(4000);
        Rent model = rentMapper.toModel(representation);

        assertEquals(representation.miles, model.getMiles());
        assertEquals(representation.startDate, model.getStartDate().toString());
        assertEquals(representation.endDate, model.getEndDate().toString());
        assertEquals(representation.customer, model.getCustomer().getId());
        assertEquals(representation.rentedVehicle, model.getRentedVehicle().getId());
        assertEquals(representation.technicalCheck, model.getTechnicalCheck().getId());

        Vehicle vehicle = vehicleRepository.findById(representation.rentedVehicle);
        assertNotNull(model.getRentedVehicle());
        assertEquals(vehicle.getId(), model.getRentedVehicle().getId());
        assertEquals(vehicle.getManufacturer(), model.getRentedVehicle().getManufacturer());
        assertEquals(vehicle.getPlateNumber(), model.getRentedVehicle().getPlateNumber());

        assertNotNull(model.getTechnicalCheck());
        assertNotNull(model.getCustomer());
    }

    @Transactional
    @Test
    public void testToRepresentation() {
        Rent model = rentRepository.findById(4000);

        assertNotNull(model.getCustomer());
        assertNotNull(model.getRentedVehicle());
        assertNotNull(model.getTechnicalCheck());

        RentRepresentation representation = rentMapper.toRepresentation(model);
        assertEquals(model.getId(), representation.id);
        assertEquals(model.getEndDate().toString(), representation.endDate);
        assertEquals(model.getMileageCost(), representation.mileageCost);
        assertEquals(model.getCustomer().getId(), representation.customer);
        assertEquals(model.getRentedVehicle().getId(), representation.rentedVehicle);
        assertEquals(model.getTechnicalCheck().getId(), representation.technicalCheck);

        Customer customer = model.getCustomer();
        Customer c = customerRepository.findById(customer.getId());
        assertNotNull(customer);
        assertNotNull(c);
        assertEquals(c.getId(), customer.getId());
        assertEquals(c.getSurname(), customer.getSurname());
        assertEquals(c.getAFM(), customer.getAFM());
        assertEquals(c.getNumber(), customer.getNumber());
    }


    private RentRepresentation createRentRepresentation(Integer id) {
        RentRepresentation representation = new RentRepresentation();
        representation.id = id;
        representation.startDate = LocalDate.of(2023,10,10).toString();
        representation.endDate = LocalDate.of(2023,10,20).toString();
        representation.rentState = RentState.Finished;
        representation.fixedCost = new Money(770);  //assume vehicle with id 3007 is rented
        representation.miles = 130;  //company with id 2001 which owns the vehicles 3007, policy: .15 -> 100, .25 -> 200
        representation.mileageCost = new Money(45.25);
        representation.damageCost = new Money(0);  //assume no damage
        representation.totalCost = new Money(representation.mileageCost.getAmount() +
                representation.fixedCost.getAmount() + representation.damageCost.getAmount());
        representation.rentedVehicle = 3007;
        representation.customer = 1000;
        representation.technicalCheck = 5000;
        return representation;
    }
}