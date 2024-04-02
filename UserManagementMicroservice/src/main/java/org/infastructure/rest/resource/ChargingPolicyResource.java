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
        return policyMapper.toRepresentationList(policyRepository.listAll());
    }

    @GET
    @Path("{policyId: [0-9]+}")
    @Transactional
    public ChargingPolicyRepresentation listPolicyById(@PathParam("policyId") Integer policyId) {
//       todo edo skaei to findByIdOptional den ksero pos na to kano, Prosorina vazo to palio
//        ChargingPolicy policy = policyRepository.findByIdOptional(policyId)
//                .orElseThrow(() -> new NotFoundException("[!] GET /policy/"+policyId+"\n\tCould not find policy with id " + policyId));
        ChargingPolicy policy = policyRepository.findById(policyId);
        if (policy == null) {
            throw new NotFoundException("[!] GET /policy/"+policyId+"\n\tCould not find policy with id " + policyId);
        }
        return policyMapper.toRepresentation(policy);
    }

    // ---------- PUT ----------

    @PUT
    @Transactional
    public Response create(ChargingPolicyRepresentation representation) {
//        todo to idio kai edo
//        if (representation.id == null || policyRepository.findByIdOptional(representation.id).isPresent()) {  //if id is null or already exists
//            throw new NotFoundException("[!] PUT /policy\n\tCould not create policy, invalid id");
//        }
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