package org.infastructure.rest.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.domain.Vehicle.Vehicle;
import org.domain.Vehicle.VehicleRepository;
import org.infastructure.rest.Representation.VehicleMapper;
import org.infastructure.rest.Representation.VehicleRepresentation;
import org.junit.jupiter.api.Test;
import org.util.IntegrationBase;
import org.util.Money;
import org.util.VehicleState;
import org.util.VehicleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class VehicleMapperTest extends IntegrationBase {

    @Inject
    VehicleMapper vehicleMapper;

    @Inject
    VehicleRepository vehicleRepository;

//    @Inject
//    CompanyRepository companyRepository;

    @Transactional
    @Test
    public void testToModel() {
        VehicleRepresentation representation = createVehicleRepresentation(3000);

        Vehicle model = vehicleMapper.toModel(representation);
        assertEquals(model.getId(), representation.id);
        assertEquals(model.getPlateNumber(), representation.plateNumber);
        assertEquals(model.getVehicleState(), representation.vehicleState);
        assertEquals(model.getYear(), representation.year);
        assertEquals(model.getId(), representation.companyId);

//        Company company = model.getCompany();
//        assertNotNull(company);
//        Company c = companyRepository.findById(company.getId());
//        assertEquals(company.getId(), c.getId());
//        assertEquals(company.getIBAN(), c.getIBAN());
//        assertEquals(company.getCity(), c.getCity());
    }

    @Transactional
    @Test
    public void testToRepresentation() {
        Vehicle model = vehicleRepository.findVehicleById(3000);
        assertNotNull(model);

        VehicleRepresentation representation = vehicleMapper.toRepresentation(model);
        assertNotNull(representation);
        assertEquals(model.getId(), representation.id);
        assertEquals(model.getYear(), representation.year);
        assertEquals(model.getPlateNumber(), representation.plateNumber);
        assertEquals(model.getVehicleType(), representation.vehicleType);
        assertEquals(model.getId(), representation.companyId);
    }


    private VehicleRepresentation createVehicleRepresentation(Integer id) {
        VehicleRepresentation representation = new VehicleRepresentation();
        representation.id = id;
        representation.manufacturer = "TOYOTA";
        representation.model = "YARIS";
        representation.year = 2015;
        representation.miles = 100000;
        representation.plateNumber = "YMB-6325";
        representation.vehicleType = VehicleType.Hatchback;
        representation.vehicleState = VehicleState.Available;
        representation.fixedCharge = new Money(30);
        representation.companyId = 2000;
        return representation;
    }
}