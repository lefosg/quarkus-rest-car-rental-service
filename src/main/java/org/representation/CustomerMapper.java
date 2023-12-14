package org.representation;

import org.domain.Company;
import org.domain.Customer;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta",
injectionStrategy = InjectionStrategy.CONSTRUCTOR,
uses = CustomerMapper.class)
public abstract class CustomerMapper {

    public abstract CustomerRepresentation toRepresentation(Customer customer);
    public abstract List<CustomerRepresentation> toRepresentationList(List<Customer> customer);
}
