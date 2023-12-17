package org.representation;

import org.domain.ChargingPolicy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
    componentModel = "jakarta",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public abstract class ChargingPolicyMapper {

    public abstract ChargingPolicyRepresentation toRepresentation(ChargingPolicy chargingPolicy);
    public abstract List<ChargingPolicyRepresentation> toRepresentationList (List<ChargingPolicy> chargingPolicies);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "mileageScale", target = "mileageScale")
    @Mapping(source = "damageType", target = "damageType")
    public abstract ChargingPolicy toModel(ChargingPolicyRepresentation representation);
}
