package org.representation;

import org.domain.Company;
import org.domain.Vehicle;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = VehicleMapper.class)
public abstract class VehicleMapper {

    public abstract VehicleRepresentation toRepresentation(Vehicle vehicle);
    public abstract List<VehicleRepresentation> toRepresentationList(List<Vehicle> vehicle);
}
