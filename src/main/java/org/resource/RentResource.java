package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.domain.Rent;
import org.persistence.RentRepository;
import org.representation.*;

import java.net.URI;
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
