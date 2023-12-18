package org.representation;

import org.domain.Company;
import org.domain.Rent;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = RentMapper.class)
public abstract class RentMapper {
    public abstract RentRepresentation toRepresentation(Rent rent);
    public abstract List<RentRepresentation> toRepresentationList(List<Rent> rent);

    @Mapping(source = "mileageCost", target = "mileageCost")
    //@Mapping(source = "technicalCheck", target = "technicalCheck", ignore = true)
    public abstract Rent toModel(RentRepresentation representation);
}
