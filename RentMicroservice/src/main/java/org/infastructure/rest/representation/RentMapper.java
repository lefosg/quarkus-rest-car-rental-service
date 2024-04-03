package org.infastructure.rest.representation;

import jakarta.inject.Inject;
import org.domain.Rents.Rent;
import org.domain.TechnicalCheck.TechnicalCheckRepository;
import org.mapstruct.*;


import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = RentMapper.class)
public abstract class RentMapper {

//    @Inject
//    CustomerRepository customerRepository;
//    @Inject
//    VehicleRepository vehicleRepository;
    @Inject
    TechnicalCheckRepository technicalCheckRepository;

    //@Mapping(source = "mileageCost", target = "mileageCost")
    @Mapping(source = "technicalCheck.id", target = "technicalCheck")
    public abstract RentRepresentation toRepresentation(Rent rent);
    public abstract List<RentRepresentation> toRepresentationList(List<Rent> rent);

    @Mapping(target = "technicalCheck", ignore = true)
    @Mapping(source = "miles", target = "miles")
    public abstract Rent toModel(RentRepresentation representation);


    @AfterMapping
    protected void fill(RentRepresentation representation, @MappingTarget Rent rent) {
       //todo for User fleet
        // rent.setCustomer(customerRepository.findById(representation.customer));
        //rent.setRentedVehicle(vehicleRepository.findById(representation.rentedVehicle));
        if (technicalCheckRepository.findTechnicalCheckById(representation.technicalCheck) != null) {
            rent.setTechnicalCheck(technicalCheckRepository.findTechnicalCheckById(representation.technicalCheck));
        }
    }
}
