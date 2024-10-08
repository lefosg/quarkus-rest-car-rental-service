package org.infastructure.rest.representation;

import org.domain.customer.Customer;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta",
injectionStrategy = InjectionStrategy.CONSTRUCTOR,
uses = CustomerMapper.class)

public abstract class CustomerMapper {

    public abstract CustomerRepresentation toRepresentation(Customer customer);
    public abstract List<CustomerRepresentation> toRepresentationList(List<Customer> customer);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "surname", target = "surname")
    @Mapping(source = "number", target = "number")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "zipcode", target = "zipcode")
    @Mapping(source = "holderName", target = "holderName")
    @Mapping(source = "expirationDate", target = "expirationDate")
    public abstract Customer toModel(CustomerRepresentation customerRepresentation);
}
