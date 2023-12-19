package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.domain.Company;
import org.domain.Customer;
import org.persistence.CustomerRepository;
import org.representation.CompanyRepresentation;
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
        @GET
        @Path("{customerId: [0-9]+}")
        @Transactional
        public CustomerRepresentation listCompanyById(@PathParam("customerId") Integer customerId) {
            Customer customer = customerRepository.findById(customerId);

            if (customer ==  null) {
                throw new NotFoundException("[!] GET /customer/"+customerId+"\n\tCould not find customer with id " + customerId);
            }
            return customerMapper.toRepresentation(customer);
        }
        //@GET
       // @Transactional
        //public String test() {
       //     return "PELATES";
       // }
    }

