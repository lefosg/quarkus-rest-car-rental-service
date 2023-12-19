package org.util;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.domain.Rent;
import org.domain.Vehicle;
import org.junit.jupiter.api.Test;
import org.representation.RentMapper;
import org.representation.RentRepresentation;
import org.representation.VehicleMapper;
import org.representation.VehicleRepresentation;

import java.time.LocalDate;

import static io.restassured.RestAssured.when;

/**
 * Just for random testing. Ignore
 */
@QuarkusTest
public class PlaygroundTest {

    //@Inject
    //VehicleMapper vehicleMapper;

    @Inject
    RentMapper rentMapper;

    @Test
    public void test() {
        RentRepresentation representation = createRentRepresentation((Integer) 4002);
        Rent rent = rentMapper.toModel(representation);

        System.out.println(rentMapper.toRepresentation(rent));
    }

    private RentRepresentation createRentRepresentation(Integer id) {
        RentRepresentation representation = new RentRepresentation();
        representation.id = id;
        representation.startDate = LocalDate.of(2023,10,10);
        representation.endDate = LocalDate.of(2023,10,20);
        representation.rentState = RentState.Finished;
        representation.fixedCost = new Money(770);  //assume vehicle with id 3007 is rented
        representation.miles = 130;  //company with id 2001 which owns the vehicles 3007, policy: .15 -> 100, .25 -> 200
        representation.mileageCost = new Money(45.25);
        representation.damageCost = new Money(0);  //assume no damage
        representation.totalCost = new Money(representation.mileageCost.getAmount() +
                representation.fixedCost.getAmount() + representation.damageCost.getAmount());
        //representation.vehicle = createVehicleRepresentation(3007);
        //representation.customer = createCustomerRepresentation(1000);
        return representation;
    }

}
