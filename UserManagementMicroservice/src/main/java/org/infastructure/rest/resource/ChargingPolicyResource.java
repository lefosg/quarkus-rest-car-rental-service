package org.infastructure.rest.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.domain.company.ChargingPolicy;
import org.domain.company.ChargingPolicyRepository;
import org.infastructure.persistence.ChargingPolicyRepositoryImpl;
import org.infastructure.rest.representation.ChargingPolicyMapper;
import org.infastructure.rest.representation.ChargingPolicyRepresentation;


import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    public List<ChargingPolicyRepresentation> listAllPolicies() {
        return policyMapper.toRepresentationList(policyRepository.listAllPolicies());
    }

    @GET
    @Path("{policyId: [0-9]+}")
    @Transactional
    public ChargingPolicyRepresentation listPolicyById(@PathParam("policyId") Integer policyId) {
        ChargingPolicy policy = policyRepository.findByPolicyIdOptional(policyId)
                .orElseThrow(() -> new NotFoundException("[!] GET /policy/"+policyId+"\n\tCould not find policy with id " + policyId));

        return policyMapper.toRepresentation(policy);
    }

    // ---------- PUT ----------

    @PUT
    @Transactional
    public Response create(ChargingPolicyRepresentation representation) {
        if (representation.id == null || policyRepository.findByPolicyIdOptional(representation.id).isPresent()) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /policy\n\tCould not create policy, invalid id");
        }

        ChargingPolicy policy = policyMapper.toModel(representation);
        policyRepository.persistPolicy(policy);
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
        policyRepository.getPolicyEntityManager().merge(policy);
        return Response.noContent().build();
    }


}