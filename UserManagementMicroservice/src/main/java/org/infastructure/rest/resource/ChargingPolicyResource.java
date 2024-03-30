package org.infastructure.rest.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.domain.ChargingPolicy;
import org.infastructure.rest.representation.ChargingPolicyMapper;
import org.infastructure.rest.representation.ChargingPolicyRepresentation;
import org.infastructure.rest.persistence.ChargingPolicyRepository;


import java.net.URI;
import java.util.List;

@Path("policy")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ChargingPolicyResource {

    @Inject
    ChargingPolicyRepository policyRepository;

    @Inject
    ChargingPolicyMapper policyMapper;

    @Inject
    UriInfo uriInfo;

    // ---------- GET ----------

    @GET
    @Transactional
    public List<ChargingPolicyRepresentation> listAllVehicles() {
        return policyMapper.toRepresentationList(policyRepository.listAll());
    }

    @GET
    @Path("{policyId: [0-9]+}")
    @Transactional
    public ChargingPolicyRepresentation listVehicleById(@PathParam("policyId") Integer policyId) {
        ChargingPolicy policy = policyRepository.findById(policyId);
        if (policy == null) {
            throw new NotFoundException("[!] GET /policy/"+policyId+"\n\tCould not find policy with id " + policyId);
        }
        return policyMapper.toRepresentation(policy);
    }

    // ---------- PUT ----------

    //@PUT
    //@Transactional
    public Response create(ChargingPolicyRepresentation representation) {
        if (representation.id == null || policyRepository.findById(representation.id) != null) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /policy\n\tCould not create policy, invalid id");
        }

        ChargingPolicy policy = policyMapper.toModel(representation);
        policyRepository.persist(policy);
        URI uri = UriBuilder.fromResource(ChargingPolicyResource.class).path(String.valueOf(policy.getId())).build();
        return Response.created(uri).entity(policyMapper.toRepresentation(policy)).build();
    }

    @PUT
    @Transactional
    @Path("{policyId: [0-9]+}")
    public Response update(@PathParam("policyId") Integer policyId, ChargingPolicyRepresentation representation) {
        if (policyId == null || !(policyId).equals(representation.id)) {
            throw new NotFoundException("[!] PUT /policy\n\tCould not update policy, id mismatching");
        }

        ChargingPolicy policy = policyMapper.toModel(representation);
        policyRepository.getEntityManager().merge(policy);
        return Response.noContent().build();
    }


}