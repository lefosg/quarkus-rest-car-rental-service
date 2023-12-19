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

@Path("TechnicalCheck")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped

public class TechnicalCheckResource {

    @Inject
    TechnicalCheckRepository technicalCheckRepository;

    @Inject
    TechnicalCheckMapper technicalCheckMapper;

    @Inject
    VehicleMapper vehicleMapper;

    @Inject
    CompanyMapper companyMapper;

    @Context
    UriInfo uriInfo;

    @GET
    @Transactional
    public List<TechnicalCheckRepresentation> listAllTechnicalChecks(@DefaultValue("") @QueryParam("technicalCheck") Integer technicalCheck) {
        return technicalCheckMapper.toRepresentationList(technicalCheckRepository.findBytechnicalCheck(technicalCheck));
    }

  @GET
  @Path("{technicalCheckId: [0-9]+}")
    @Transactional
    public TechnicalCheckRepresentation listTechnicalCheckById(@PathParam("technicalCheckId") Integer technicalCheckId) {
     TechnicalCheck technicalCheck = technicalCheckRepository.findById(technicalCheckId);

      if (technicalCheck ==  null) {
           throw new NotFoundException("[!] GET /technicalCheck/"+technicalCheckId+"\n\tCould not find technicalCheck with id " + technicalCheckId);
       }
        return technicalCheckMapper.toRepresentation(technicalCheck);
    }


    //@GET
   // @Path("{technicalCheckId: [0-9]+}/rents")
   // @Transactional
   // public List<TechnicalCheckRepresentation> listTechnicalCheckRents(@PathParam("technicalCheckId") Integer technicalCheckId) {
      //  TechnicalCheck technicalCheck = technicalCheckRepository.findById(technicalCheckId);

     //   if (technicalCheck ==  null) {
       //     throw new NotFoundException("[!] GET /technicalCheck/"+technicalCheckId+"\n\tCould not find technicalCheck");
       // }
     //   return RentMapper.toRepresentationList(technicalCheck.getRent());
   // }
}


