package org.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import jakarta.enterprise.context.RequestScoped;
import org.domain.Company;
import org.domain.Customer;

import java.util.List;

@RequestScoped
public class CustomerRepository implements PanacheRepositoryBase<Customer,Integer> {
    public List<Customer> findBySurname(String surname) {
        if (surname == null)
            return listAll();

        return find("select customer from Customer customer where customer.surname = :surname",
        Parameters.with("surname", surname).map()).list();
    }



}
