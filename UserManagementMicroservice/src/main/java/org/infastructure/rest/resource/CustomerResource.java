package org.infastructure.rest.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.domain.customer.Customer;
import org.domain.customer.CustomerRepository;
import org.infastructure.rest.representation.CustomerMapper;
import org.infastructure.rest.representation.CustomerRepresentation;

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

    // ---------- GET ----------

    @GET
    @Transactional
    public List<CustomerRepresentation> listAllCustomers() {
        return customerMapper.toRepresentationList(customerRepository.listAllCustomers());
    }
    @GET
    @Path("{customerId: [0-9]+}")
    @Transactional
    public CustomerRepresentation listCompanyById(@PathParam("customerId") Integer customerId) {
        Customer customer = customerRepository.findByIdOptional(customerId)
                .orElseThrow(() -> new NotFoundException("[!] GET /customer/"+customerId+"\n\tCould not find customer with id " + customerId));

        return customerMapper.toRepresentation(customer);
    }

    // ---------- PUT ----------
    @PUT
    @Transactional
    public Response create(CustomerRepresentation representation) {
        if (representation.id == null || customerRepository.findByIdOptional(representation.id).isPresent()) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /customer\n\tCould not create customer, invalid id");
        }
        Customer customer = customerMapper.toModel(representation);
        customerRepository.persist(customer);
        URI uri = UriBuilder.fromResource(CustomerResource.class).path(String.valueOf(customer.getId())).build();
        return Response.created(uri).entity(customerMapper.toRepresentation(customer)).build();
    }
    @PUT
    @Transactional
    @Path("/{customerId:[0-9]+}")
    public Response update(@PathParam("customerId") Integer customerId, CustomerRepresentation representation) {
        if (! customerId.equals(representation.id)) {
            throw new RuntimeException("[!] PUT /customer " + customerId + "\n\tCould not update company, id mismatching");
        }

        Customer customer = customerMapper.toModel(representation);
        customerRepository.getEntityManager().merge(customer);
        return Response.noContent().build();
    }

//   todo for fleet

//    @GET
//    @Transactional
//    @Path("/availableVehicles/{startDate}/{endDate}")
//    public Response getAvailableVehicles(
//            @PathParam("startDate") String startDate,
//            @PathParam("endDate") String endDate) {
//        LocalDate startLocalDate = null;
//        LocalDate endLocalDate = null;
//        if (startDate != null && endDate != null) {
//            startLocalDate = LocalDate.parse(startDate);
//            endLocalDate = LocalDate.parse(endDate);
//        } else {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid date parameters").build();
//        }
//        if (startLocalDate.isAfter(endLocalDate)) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Start date is after end date").build();
//        }
//        List<Vehicle> availableVehicles = vehicleRepository.findByState(VehicleState.Available);
//        List<VehicleRepresentation> availableVehiclesRep = vehicleMapper.toRepresentationList(availableVehicles);
//
//        if (!availableVehicles.isEmpty()) {
//            return Response.ok(availableVehiclesRep).build();
//        } else {
//            return Response.noContent().build();
//        }
//    }

    // ---------- POST ----------

    
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
        if (customerId == null || customerRepository.findByIdOptional(customerId).isEmpty()) {
            throw new NotFoundException("[!] DELETE /customer " + customerId + "\n\tCould not find customer with id " + customerId);
        }

        customerRepository.deleteCustomer(customerId);
        boolean deleted = customerRepository.findByIdOptional(customerId).isEmpty();
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /customer " + customerId + "\n\tCould not delete customer with id " + customerId);
        }
        return Response.status(200).build();
    }

}
