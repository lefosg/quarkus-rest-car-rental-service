package org.infastructure.rest.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.domain.Rents.Rent;
import org.domain.Rents.RentRepository;

import org.junit.jupiter.api.Test;

import org.util.Fixture;
import org.util.IntegrationBase;
import org.util.Money;
import org.util.RentState;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class RentMapperTest extends IntegrationBase {

    @Inject
    RentMapper rentMapper;

    @Inject
    RentRepository rentRepository;

    @Test
    public void testToModel() {

        RentRepresentation representation = Fixture.createRentRepresentation(4000,1000,3000);
        representation.technicalCheck = 5001;
        Rent model = rentMapper.toModel(representation);

        assertEquals(representation.miles, model.getMiles());
        assertEquals(representation.startDate, model.getStartDate().toString());
        assertEquals(representation.endDate, model.getEndDate().toString());
        assertEquals(representation.customerId, model.getCustomerId());
        assertEquals(representation.vehicleId, model.getVehicleId());
        assertEquals(representation.technicalCheck, model.getTechnicalCheck().getId());

        assertNotNull(model.getTechnicalCheck());
    }

    @Transactional
    @Test
    public void testToRepresentation() {
        Rent model = rentRepository.findRentById(4000);


        assertNotNull(model.getTechnicalCheck());

        RentRepresentation representation = rentMapper.toRepresentation(model);
        assertEquals(model.getId(), representation.id);
        assertEquals(model.getEndDate().toString(), representation.endDate);
        assertEquals(model.getMileageCost(), representation.mileageCost);
        assertEquals(model.getCustomerId(), representation.customerId);
        assertEquals(model.getVehicleId(), representation.vehicleId);
        assertEquals(model.getTechnicalCheck().getId(), representation.technicalCheck);

    }


}