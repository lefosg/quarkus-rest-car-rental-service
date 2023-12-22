package org.representation;

import jakarta.inject.Inject;
import org.domain.Rent;
import org.mapstruct.*;
import org.persistence.CustomerRepository;
import org.persistence.TechnicalCheckRepository;
import org.persistence.VehicleRepository;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = RentMapper.class)
public abstract class RentMapper {

    @Inject
    CustomerRepository customerRepository;
    @Inject
    VehicleRepository vehicleRepository;
    @Inject
    TechnicalCheckRepository technicalCheckRepository;

    //@Mapping(source = "mileageCost", target = "mileageCost")
    @Mapping(source = "technicalCheck.id", target = "technicalCheck")
    @Mapping(source = "customer.id", target = "customer")
    @Mapping(source = "rentedVehicle.id", target = "rentedVehicle")
    public abstract RentRepresentation toRepresentation(Rent rent);
    public abstract List<RentRepresentation> toRepresentationList(List<Rent> rent);

    @Mapping(target = "technicalCheck", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "rentedVehicle", ignore = true)
    @Mapping(source = "miles", target = "miles")
    public abstract Rent toModel(RentRepresentation representation);

    @AfterMapping
    protected void fill(RentRepresentation representation, @MappingTarget Rent rent) {
        rent.setCustomer(customerRepository.findById(representation.customer));
        rent.setRentedVehicle(vehicleRepository.findById(representation.rentedVehicle));
        if (technicalCheckRepository.findById(representation.technicalCheck) != null)
            rent.setTechnicalCheck(technicalCheckRepository.findById(representation.technicalCheck));
    }
}
