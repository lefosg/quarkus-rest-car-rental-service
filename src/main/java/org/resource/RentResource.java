package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.domain.Customer;
import org.domain.Rent;
import org.domain.Vehicle;
import org.persistence.CustomerRepository;
import org.persistence.RentRepository;
import org.persistence.VehicleRepository;
import org.representation.*;
import org.util.VehicleState;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Path("rent")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class RentResource {

    @Inject
    RentRepository rentRepository;

    @Inject
    RentMapper rentMapper;

    @Inject
    CustomerMapper customerMapper;

    @Inject
    VehicleMapper vehicleMapper;

    @Inject
    TechnicalCheckMapper technicalCheckMapper;

    @Inject
    CustomerRepository customerRepository;

    @Inject
    VehicleRepository vehicleRepository;

    @Context
    UriInfo uriInfo;

    // ---------- GET ----------

    @GET
    @Transactional
    public List<RentRepresentation> listAllRents(@DefaultValue("0") @QueryParam("month") Integer month) {
        return rentMapper.toRepresentationList(rentRepository.findByMonth(month));
    }

    @GET
    @Transactional
    @Path("{rentId: [0-9]+}")
    public RentRepresentation listRentById(@PathParam("rentId") Integer rentId) {
        Rent rent = rentRepository.findById(rentId);
        if (rent == null) {
            throw new NotFoundException("[!] GET /rent/"+rentId+"\n\tCould not find rent with id " + rentId);
        }

        return rentMapper.toRepresentation(rent);
    }
    //todo: add /rent/rentid/policy

    @GET
    @Transactional
    @Path("{rentId: [0-9]+}/customer")
    public CustomerRepresentation listCustomerOfRent(@PathParam("rentId") Integer rentId) {
        if (rentId == null || rentRepository.findById(rentId) == null) {  //if id null or not in db
            throw new NotFoundException("[!] GET /rent/"+rentId+"\n\tCould not find customer for rent with id " + rentId);
        }
        Rent rent = rentRepository.findById(rentId);
        return customerMapper.toRepresentation(rent.getCustomer());
    }

    @GET
    @Transactional
    @Path("{rentId: [0-9]+}/vehicle")
    public VehicleRepresentation listVehicleOfRent(@PathParam("rentId") Integer rentId) {
        if (rentId == null || rentRepository.findById(rentId) == null) {  //if id null or not in db
            throw new NotFoundException("[!] GET /rent/"+rentId+"\n\tCould not find vehicle for rent with id " + rentId);
        }
        Rent rent = rentRepository.findById(rentId);
        return vehicleMapper.toRepresentation(rent.getRentedVehicle());
    }

    @GET
    @Transactional
    @Path("{rentId: [0-9]+}/technicalCheck")
    public TechnicalCheckRepresentation listTechnicalCheckOfRent(@PathParam("rentId") Integer rentId) {
        if (rentId == null || rentRepository.findById(rentId) == null) {  //if id null or not in db
            throw new NotFoundException("[!] GET /rent/"+rentId+"\n\tCould not find rent for rent with id " + rentId);
        }
        Rent rent = rentRepository.findById(rentId);
        return technicalCheckMapper.toRepresentation(rent.getTechnicalCheck());
    }

    // ---------- PUT ----------

    //@PUT
    //@Transactional
    public Response create(RentRepresentation representation) {
        if (representation.id == null || rentRepository.findById(representation.id) != null) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /rent\n\tCould not create rent, invalid id");
        }
        Rent rent = rentMapper.toModel(representation);
        rentRepository.persist(rent);
        URI uri = UriBuilder.fromResource(RentResource.class).path(String.valueOf(rent.getId())).build();
        return Response.created(uri).entity(rentMapper.toRepresentation(rent)).build();
    }

    @POST
    @Transactional
    @Path("/newRent/{customerId:[0-9]+}")
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
    @Path("/returnVehicle/{customerId:[0-9]+}")
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

//        for (Rent rent : customer.getRents()) {
//            if (rent.getRentedVehicle().equals(vehicle)) {
//                if (rent.getRentedVehicle().getVehicleState() != VehicleState.Available) {
//                    return Response.status(Response.Status.BAD_REQUEST).entity("This vehicle cannot be returned").build();
//                }
//            }
//        }

        if (vehicle.getVehicleState() == VehicleState.Available) {
            return Response.status(Response.Status.BAD_REQUEST).entity("This vehicle cannot be returned").build();
        }

        customer.returnVehicle(vehicle, miles);
        customerRepository.getEntityManager().merge(customer);
        return Response.status(Response.Status.OK).entity("Vehicle returned").build();

    }

    @PUT
    @Transactional
    @Path("{rentId: [0-9]+}")
    public Response update(@PathParam("rentId") Integer rentId, RentRepresentation representation) {
        if (rentId == null || !(rentId).equals(representation.id)) {
            throw new NotFoundException("[!] PUT /rent\n\tCould not update rent, id mismatching");
        }
        Rent rent = rentMapper.toModel(representation);
        rentRepository.getEntityManager().merge(rent);
        return Response.noContent().build();
    }

    // ---------- DELETE ----------

    @DELETE
    @Transactional
    public Response deleteAllRents() {
        rentRepository.deleteAllRents();
        return Response.status(200).build();
    }

    @DELETE
    @Transactional
    @Path("{rentId: [0-9]+}")
    public Response deleteCompany(@PathParam("rentId") Integer rentId) {
        if (rentId == null || rentRepository.findById(rentId) == null) {
            throw new NotFoundException("[!] DELETE /rent" + rentId + "\n\tCould not find rent with id " + rentId);
        }

        rentRepository.deleteRent(rentId);
        boolean deleted = rentRepository.findById(rentId) == null;
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /rent" + rentId + "\n\tCould not delete rent with id " + rentId);
        }
        return Response.status(200).build();
    }

}
