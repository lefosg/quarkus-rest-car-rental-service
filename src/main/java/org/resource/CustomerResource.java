package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.domain.Company;
import org.domain.Customer;
import org.domain.Rent;
import org.domain.Vehicle;
import org.persistence.CustomerRepository;
import org.persistence.VehicleRepository;
import org.representation.*;
import org.util.VehicleState;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Path("customer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class CustomerResource {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleMapper vehicleMapper;

    @Inject
    CustomerMapper customerMapper;

    // ---------- GET ----------

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

    // ---------- PUT ----------
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

    @GET
    @Transactional
    @Path("/availableVehicles/{startDate}/{endDate}")
    public Response getAvailableVehicles(
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate) {
        LocalDate startLocalDate = null;
        LocalDate endLocalDate = null;
        if (startDate != null && endDate != null) {
            startLocalDate = LocalDate.parse(startDate);
            endLocalDate = LocalDate.parse(endDate);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid date parameters").build();
        }
        if (startLocalDate.isAfter(endLocalDate)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Start date is after end date").build();
        }
        List<Vehicle> availableVehicles = vehicleRepository.findByState(VehicleState.Available);
        List<VehicleRepresentation> availableVehiclesRep = vehicleMapper.toRepresentationList(availableVehicles);

        if (!availableVehicles.isEmpty()) {
            return Response.ok(availableVehiclesRep).build();
        } else {
            return Response.noContent().build();
        }
    }

    // ---------- POST ----------

    @POST
    @Transactional
    @Path("{customerId:[0-9]+}/rent")
    public Response makeRent(
        @QueryParam("startDate") String start,
        @QueryParam("endDate") String end,
        @QueryParam("vehicleId") Integer vehicleId,
        @PathParam("customerId") Integer customerId) {

        LocalDate startDate = null;
        LocalDate endDate = null;
        if ((start != null && end != null) && (!start.equals("") && !end.equals(""))) {
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid date parameters").build();
        }
        if (customerId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid customer id").build();
        }
        if (vehicleId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid vehicle id").build();
        }

        Customer customer = customerRepository.findById(customerId);
        Vehicle vehicle = vehicleRepository.findById(vehicleId);

        if (customer == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer does not exist").build();
        }
        if (vehicle == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Vehicle does not exist").build();
        }

        if (vehicle.getVehicleState() != VehicleState.Available) {
            return Response.status(Response.Status.OK).entity("Vehicle not available until " + end).build();
        }

        customer.rent(startDate, endDate, vehicle);
        customerRepository.getEntityManager().merge(customer);
        return Response.status(Response.Status.OK).entity("You rented the vehicle").build();
    }

    @POST
    @Transactional
    @Path("{customerId:[0-9]+}/returnVehicle")
    public Response returnVehicle(
            @PathParam("customerId") Integer customerId,
            @QueryParam("vehicleId") Integer vehicleId,
            @QueryParam("miles") float miles
    ) {
        if (customerId == null || customerRepository.findById(customerId) == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer does not exist or id was null").build();
        }
        if (vehicleId == null || vehicleRepository.findById(vehicleId) == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Vehicle does not exist or id was null").build();
        }
        if (miles <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Miles provided are negative").build();
        }

        Customer customer = customerRepository.findById(customerId);
        Vehicle vehicle = vehicleRepository.findById(vehicleId);

        for (Rent rent : customer.getRents()) {
            if (rent.getRentedVehicle().equals(vehicle)) {
                if (rent.getRentedVehicle().getVehicleState() != VehicleState.Available) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("This vehicle cannot be returned").build();
                }
            }
        }

        if (vehicle.getVehicleState() != VehicleState.Available) {
            return Response.status(Response.Status.BAD_REQUEST).entity("This vehicle cannot be returned").build();
        }

        customer.returnVehicle(vehicle, miles);
        customerRepository.getEntityManager().merge(customer);
        return Response.status(Response.Status.OK).entity("Vehicle returned").build();

    }

    
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

