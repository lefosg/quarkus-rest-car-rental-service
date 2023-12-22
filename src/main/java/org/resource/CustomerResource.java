package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

    // ---------- GET ----------

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

    // ---------- PUT ----------



    // ---------- DELETE ----------

    @DELETE
    @Transactional
    public Response deleteAllCustomers() {
        customerRepository.deleteAllCustomers();
        return Response.status(200).build();
    }

    @DELETE
    @Transactional
    @Path("{customerId: [0-9]+}")
    public Response deleteCustomer(@PathParam("customerId") Integer customerId) {
        if (customerId == null || customerRepository.findById(customerId) == null) {
            throw new NotFoundException("[!] DELETE /customer " + customerId + "\n\tCould not find customer with id " + customerId);
        }

        customerRepository.deleteCustomer(customerId);
        boolean deleted = customerRepository.findById(customerId) == null;
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /customer " + customerId + "\n\tCould not delete customer with id " + customerId);
        }
        return Response.status(200).build();
    }

}

