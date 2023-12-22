package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.domain.Customer;
import org.persistence.CustomerRepository;
import org.representation.CustomerMapper;
import org.representation.CustomerRepresentation;

import java.net.URI;
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
        @PUT
        @Transactional
        public Response create(CustomerRepresentation representation) {
            if (representation.id == null ||  customerRepository.findById(representation.id) != null ) {  //if id is null or already exists
                throw new NotFoundException("[!] PUT /customer\n\tCould not create customer, invalid id");
            }

            Customer customer = customerMapper.toModel(representation);
            customerRepository.persist(customer);
            URI uri = UriBuilder.fromResource(CustomerResource.class).path(String.valueOf(customer.getId())).build();
            return Response.created(uri).entity(customerMapper.toRepresentation(customer)).build();
        }

        @DELETE
        @Transactional
        public Response deleteAllCustomers() {
            customerRepository.deleteAll();
            return Response.status(200).build();
        }

        //@GET
       // @Transactional
        //public String test() {
       //     return "PELATES";
       // }
    }

