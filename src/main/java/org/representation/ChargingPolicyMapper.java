package org.representation;

import org.domain.ChargingPolicy;
import org.domain.Company;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = ChargingPolicyMapper.class)
public abstract class ChargingPolicyMapper {

    public abstract ChargingPolicyRepresentation toRepresentation(ChargingPolicy chargingPolicy);
    public abstract List<ChargingPolicyRepresentation> toRepresentationList(List<ChargingPolicy> chargingPolicy);
}
