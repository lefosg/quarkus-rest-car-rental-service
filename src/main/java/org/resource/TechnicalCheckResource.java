package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.domain.Company;
import org.domain.Rent;
import org.domain.TechnicalCheck;
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
}



