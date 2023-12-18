package org.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.domain.Rent;
import org.persistence.RentRepository;
import org.representation.CustomerRepresentation;
import org.representation.RentMapper;
import org.representation.RentRepresentation;
import org.representation.VehicleRepresentation;

import java.net.URI;
import java.util.List;

@Path("rent")
public class RentResource {

    @Inject
    RentRepository rentRepository;

    @Inject
    RentMapper rentMapper;

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

//    @GET
//    @Transactional
//    @Path("{rentId: [0-9]+}/customer")
//    public CustomerRepresentation listCustomerOfRent() {
//
//    }

//    @GET
//    @Transactional
//    @Path("{rentId: [0-9]+}/vehicle")
//    public VehicleRepresentation listVehicleOfRent() {
//
//    }

    // ---------- PUT ----------

    @PUT
    @Transactional
    public Response create(RentRepresentation representation) {
        if (representation.id == null || rentRepository.findById(representation.id) != null) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /rent\n\tCould not create rent, invalid id");
        }

        Rent rent = rentMapper.toModel(representation);
        rentRepository.persist(rent);
        URI uri = UriBuilder.fromResource(RentResource.class).path(String.valueOf(rent.getId())).build();
        return Response.created(uri).entity(rentMapper.toRepresentation(rent)).build();
    }

}
