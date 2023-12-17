package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.domain.Customer;
import org.persistence.CustomerRepository;
import org.representation.CustomerMapper;
import org.representation.CustomerRepresentation;

import java.util.List;

@Path("customer")

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestScoped
    public class CustomerResource {
        @Inject
        CustomerRepository customerRepository;

        @Inject
        CustomerMapper customerMapper;

        @GET
        @Transactional
        public List<CustomerRepresentation> listAllCustomers() {
            return customerMapper.toRepresentationList(customerRepository.listAll());
        }

        //@GET
       // @Transactional
        //public String test() {
       //     return "PELATES";
       // }
    }

