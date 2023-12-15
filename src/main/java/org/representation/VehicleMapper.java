package org.representation;

import org.domain.Company;
import org.domain.Vehicle;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = VehicleMapper.class)
public abstract class VehicleMapper {

    public abstract VehicleRepresentation toRepresentation(Vehicle vehicle);
    public abstract List<VehicleRepresentation> toRepresentationList(List<Vehicle> vehicle);

    @Mapping(source = "manufacturer", target = "manufacturer")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "model", target = "model")
    @Mapping(source = "year", target = "year")
    @Mapping(source = "miles", target = "miles")
    @Mapping(source = "plateNumber", target = "plateNumber")
    @Mapping(source = "vehicleType", target = "vehicleType")
    @Mapping(source = "vehicleState", target = "vehicleState")


    public abstract Vehicle toModel(VehicleRepresentation representation);
}
