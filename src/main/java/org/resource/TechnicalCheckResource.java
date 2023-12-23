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
import org.util.Constants;
import org.util.DamageType;

import java.net.URI;
import java.util.ArrayList;
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
    public List<TechnicalCheckRepresentation> listAllTechnicalChecks(@QueryParam("damageType") String damageType ) {
        if (damageType == null || damageType.equals("")) {
            return technicalCheckMapper.toRepresentationList(technicalCheckRepository.listAll());
        }
        if (!Constants.contains(damageType)) {
            return technicalCheckMapper.toRepresentationList(new ArrayList<TechnicalCheck>());
        }
        DamageType type = DamageType.valueOf(damageType);
        return technicalCheckMapper.toRepresentationList(technicalCheckRepository.findByDamageType(type));
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
    @PUT
    @Path("{technicalCheckId:[0-9]+}")
    @Transactional
    public Response update(@PathParam("technicalCheckId") Integer technicalCheckId, TechnicalCheckRepresentation representation) {
        if (technicalCheckId == null || !(technicalCheckId).equals(representation.id)) {
            throw new NotFoundException("[!] PUT /technicalCheck\n\tCould not update technicalCheck, id mismatching");
        }
        TechnicalCheck technicalCheck = technicalCheckMapper.toModel(representation);
        System.out.println(technicalCheck);
        technicalCheckRepository.getEntityManager().merge(technicalCheck);
        return Response.noContent().build();
    }
}



