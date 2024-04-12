package org.infastructure.rest.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.domain.TechnicalCheck.TechnicalCheck;
import org.domain.TechnicalCheck.TechnicalCheckRepository;
import org.infastructure.rest.representation.RentMapper;
import org.infastructure.rest.representation.RentRepresentation;
import org.infastructure.rest.representation.TechnicalCheckMapper;
import org.infastructure.rest.representation.TechnicalCheckRepresentation;
import org.util.Constants;
import org.util.DamageType;

import java.util.ArrayList;
import java.util.List;

import static org.infastructure.rest.ApiPath.Root.CHECKS;

@Path(CHECKS)
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

    // ---------- GET ----------

    @GET
    @Transactional
    public List<TechnicalCheckRepresentation> listAllTechnicalChecks(@QueryParam("damageType") String damageType ) {
        if (damageType == null || damageType.equals("")) {
            return technicalCheckMapper.toRepresentationList(technicalCheckRepository.listAllTechnicalChecks());
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
        TechnicalCheck technicalCheck = technicalCheckRepository.findTechnicalCheckByIdOptional(technicalCheckId)
                .orElseThrow(() -> new NotFoundException("[!] GET /technical Check/"+technicalCheckId+"\n\tCould not find technical check with id " + technicalCheckId));
        return technicalCheckMapper.toRepresentation(technicalCheck);
    }


    // ---------- PUT ----------

    @PUT
    @Path("{technicalCheckId:[0-9]+}")
    @Transactional
    public Response update(@PathParam("technicalCheckId") Integer technicalCheckId, TechnicalCheckRepresentation representation) {
        if (technicalCheckId == null || !(technicalCheckId).equals(representation.id)) {
            throw new NotFoundException("[!] PUT /technicalCheck\n\tCould not update technicalCheck, id mismatching");
        }
        TechnicalCheck technicalCheck = technicalCheckMapper.toModel(representation);
        System.out.println(technicalCheck);
        technicalCheckRepository.getEntityManagerTechnicalCheck().merge(technicalCheck);
        return Response.noContent().build();
    }

    // ---------- DELETE ----------

    @DELETE
    @Transactional
    public Response deleteAllTechnicalChecks() {
        technicalCheckRepository.deleteAllTechnicalChecks();
        return Response.status(200).build();
    }

    @DELETE
    @Transactional
    @Path("{technicalCheckId: [0-9]+}")
    public Response deleteTechnicalCheck(@PathParam("technicalCheckId") Integer technicalCheckId) {
        if (technicalCheckId == null || technicalCheckRepository.findTechnicalCheckByIdOptional(technicalCheckId).isEmpty()) {
            throw new NotFoundException("[!] DELETE /technicalCheck" + technicalCheckId + "\n\tCould not find technical check with id " + technicalCheckId);
        }

        technicalCheckRepository.deleteTechnicalCheck(technicalCheckId);
        boolean deleted = technicalCheckRepository.findTechnicalCheckByIdOptional(technicalCheckId).isEmpty();
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /technicalCheck" + technicalCheckId + "\n\tCould not delete technical check with id " + technicalCheckId);
        }
        return Response.status(200).build();
    }
}



