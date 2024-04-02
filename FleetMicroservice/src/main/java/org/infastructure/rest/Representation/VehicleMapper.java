package org.infastructure.rest.Representation;

import jakarta.inject.Inject;
import org.domain.Vehicle.Vehicle;
import org.mapstruct.*;


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
    @Mapping(source = "companyId", target = "companyId")
    public abstract Vehicle toModel(VehicleRepresentation representation);

//    @AfterMapping
//    protected void fillCompany(VehicleRepresentation representation, @MappingTarget Vehicle vehicle) {
//        vehicle.setCompany(companyRepository.findById(representation.company));
//    }

}
