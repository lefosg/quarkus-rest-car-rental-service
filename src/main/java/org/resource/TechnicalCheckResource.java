package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.domain.Company;
import org.domain.Rent;
import org.domain.TechnicalCheck;
import org.domain.Vehicle;
import org.persistence.CompanyRepository;
import org.persistence.TechnicalCheckRepository;
import org.representation.*;

import java.net.URI;
import java.util.List;

@Path("technicalCheck")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped

public class TechnicalCheckResource {

    @Inject
    TechnicalCheckRepository technicalCheckRepository;

    @Inject
    TechnicalCheckMapper technicalCheckMapper;


    @Inject
    RentMapper rentMapper;


    @Context
    UriInfo uriInfo;

    @GET
    @Transactional
    public List<TechnicalCheckRepresentation> listAllTechnicalChecks()  {
        return technicalCheckMapper.toRepresentationList(technicalCheckRepository.listAll());
    }

    @GET
    @Path("{technicalCheckId: [0-9]+}")
    @Transactional
    public TechnicalCheckRepresentation listTechnicalCheckById(@PathParam("technicalCheckId") Integer technicalCheckId) {
        TechnicalCheck technicalCheck = technicalCheckRepository.findById(technicalCheckId);

        if (technicalCheck == null) {
            throw new NotFoundException("[!] GET /technicalCheck/" + technicalCheckId + "\n\tCould not find technicalCheck with id " + technicalCheckId);
        }
        return technicalCheckMapper.toRepresentation(technicalCheck);
    }


    @GET
    @Path("{technicalCheckId: [0-9]+}/rent")
    @Transactional
    public RentRepresentation listRentOfTechnicalCheck(@PathParam("technicalCheckId") Integer technicalCheckId) {
        TechnicalCheck technicalCheck = technicalCheckRepository.findById(technicalCheckId);

        if (technicalCheck == null) {
            throw new NotFoundException("[!] GET /technicalCheck/" + technicalCheckId + "\n\tCould not find technicalCheck");
        }
        return rentMapper.toRepresentation(technicalCheck.getRent());
    }
    //-------PUT--------

    @PUT
    @Transactional
    public Response create(TechnicalCheckRepresentation representation) {
        if (representation.id == null || technicalCheckRepository.findById(representation.id) != null) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /technicalCheck\n\tCould not create technicalCheck, invalid id");
        }

        TechnicalCheck technicalCheck = technicalCheckMapper.toModel(representation);
        technicalCheckRepository.persist(technicalCheck);
        URI uri = UriBuilder.fromResource(TechnicalCheckResource.class).path(String.valueOf(technicalCheck.getId())).build();
        return Response.created(uri).entity(technicalCheckMapper.toRepresentation(technicalCheck)).build();
    }

    @PUT
    @Path("{technicalCheckId:[0-9]+}")
    @Transactional
    public Response update(@PathParam("technicalCheckId") Integer technicalCheckId, TechnicalCheckRepresentation representation) {
        if (technicalCheckId == null || !(technicalCheckId).equals(representation.id)) {
            throw new NotFoundException("[!] PUT /technicalCheck\n\tCould not update technicalCheck, id mismatching");
        }

        TechnicalCheck technicalCheck = technicalCheckMapper.toModel(representation);
        technicalCheckRepository.getEntityManager().merge(technicalCheck);
        return Response.noContent().build();
    }

    //-----DELETE----

    @DELETE
    @Transactional
    public Response deleteAll() {
        technicalCheckRepository.deleteAll();
        return Response.status(200).build();
    }
    @DELETE
    @Path("{technicalCheckId: [0-9]+}")
    @Transactional
    public Response deletetechnicalCheck(@PathParam("technicalCheckId") Integer technicalCheckId) {
        if (technicalCheckId == null || technicalCheckRepository.findById(technicalCheckId) == null) {
            throw new NotFoundException("[!] DELETE /technicalCheck" + technicalCheckId + "\n\tCould not delete technicalCheck with id " + technicalCheckId + "(id not found)");
        }

        boolean deleted = technicalCheckRepository.deleteById(technicalCheckId);
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /technicalCheck" + technicalCheckId + "\n\tCould not delete technicalCheck with id " + technicalCheckId);
        }
        return Response.status(200).build();
    }
}



