package org.representation;

import org.domain.Company;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = ChargingPolicyMapper.class)
public abstract class CompanyMapper {

    //public abstract CompanyRepresentation toRepresentation(Company company);
    //public abstract List<CompanyRepresentation> toRepresentationList(List<Company> company);

}
