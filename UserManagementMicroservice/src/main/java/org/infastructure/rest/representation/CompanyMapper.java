package org.infastructure.rest.representation;

import org.domain.company.Company;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
    componentModel = "jakarta",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = ChargingPolicyMapper.class
)
public abstract class CompanyMapper {

    public abstract CompanyRepresentation toRepresentation(Company company);
    public abstract List<CompanyRepresentation> toRepresentationList(List<Company> company);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipcode", target = "zipcode")
    //@Mapping(source = "policy", target = "policy")
    public abstract Company toModel(CompanyRepresentation companyRepresentation);

}
