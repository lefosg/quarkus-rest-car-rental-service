package org.representation;

import org.domain.Rent;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = RentMapper.class)
public abstract class RentMapper {

    //@Mapping(source = "mileageCost", target = "mileageCost")
    public abstract RentRepresentation toRepresentation(Rent rent);
    public abstract List<RentRepresentation> toRepresentationList(List<Rent> rent);

    @Mapping(target = "technicalCheck", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "rentedVehicle", ignore = true)
    @Mapping(source = "miles", target = "miles")
    public abstract Rent toModel(RentRepresentation representation);

//    @AfterMapping
//    protected void setMileageCostDTO(Rent rent, @MappingTarget RentRepresentation representation) {
//        rent.setMileageCost(rent.getRentedVehicle().getCompany().calculateMileageCost(rent.getMiles()));
//        representation.mileageCost = rent.getMileageCost();
//    }
//
//    @AfterMapping
//    protected void setMileageCostDomain(RentRepresentation representation, @MappingTarget Rent rent) {
//        rent.setMileageCost(representation.mileageCost);
//    }
}
