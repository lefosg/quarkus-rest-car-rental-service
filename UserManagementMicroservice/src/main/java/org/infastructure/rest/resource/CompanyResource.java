package org.infastructure.rest.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
//import org.application.UserService;
import org.application.UserService;
import org.domain.company.ChargingPolicy;
import org.domain.company.ChargingPolicyRepository;
import org.domain.company.Company;
import org.domain.company.CompanyRepository;
import org.infastructure.persistence.CompanyRepositoryImpl;
import org.infastructure.rest.ApiPath;
import org.infastructure.rest.representation.*;
import org.infastructure.service.fleetManagament.representation.VehicleRepresentation;
import org.util.DamageType;

import java.lang.annotation.Repeatable;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

import static org.infastructure.rest.ApiPath.Root.COMPANY;

@Path(COMPANY)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class CompanyResource {

    @Inject
    CompanyRepository companyRepository;

    @Inject
    CompanyMapper companyMapper;

    @Inject
    ChargingPolicyRepository policyRepository;

    @Inject
    ChargingPolicyMapper policyMapper;

    @Inject
    UserService userService;
    @Context
    UriInfo uriInfo;

    // ---------- GET ----------

    @GET
    @Transactional
    public List<CompanyRepresentation> listAllCompanies(@DefaultValue("") @QueryParam("city") String city) {
        return companyMapper.toRepresentationList(companyRepository.findByCity(city));  //find by city will check for empty strings etc
    }

    @GET
    @Path("{companyId: [0-9]+}")
    @Transactional
    public CompanyRepresentation listCompanyById(@PathParam("companyId") Integer companyId) {
       Company company = companyRepository.findByCompanyIdOptional(companyId)
               .orElseThrow(() -> new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company with id " + companyId));

        return companyMapper.toRepresentation(company);
    }

   // todo na grapso testakia
    @GET
    @Path("/{companyId: [0-9]+}/vehicles")
    @Transactional
    public Response listCompanyVehicles(@PathParam("companyId") Integer companyId) {
        Company company = companyRepository.findCompanyById(companyId);
        if (company ==  null) {
            throw new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company");
        }

        List<VehicleRepresentation> vehicles = userService.getFleet(companyId);
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("{companyId: [0-9]+}/policy")
    @Transactional
    public ChargingPolicyRepresentation listCompanyPolicy(@PathParam("companyId") Integer companyId) {
//        Company company = companyRepository.findByIdOptional(companyId)
//                .orElseThrow(() -> new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company"));
        Company company = companyRepository.findCompanyById(companyId);

        if (company ==  null) {
            throw new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company");
        }
        return policyMapper.toRepresentation(company.getPolicy());
    }

    // ---------- PUT ----------

    @PUT
    @Transactional
    public Response create(CompanyRepresentation representation) {
        if (representation.id == null || companyRepository.findByCompanyIdOptional(representation.id).isPresent()) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /company\n\tCould not create company, invalid id");
        }
        Company company = companyMapper.toModel(representation);
        companyRepository.persistCompany(company);
        URI uri = UriBuilder.fromResource(CompanyResource.class).path(String.valueOf(company.getId())).build();
        return Response.created(uri).entity(companyMapper.toRepresentation(company)).build();
    }

    @PUT
    @Transactional
    @Path("/{companyId:[0-9]+}")
    public Response update(@PathParam("companyId") Integer companyId, CompanyRepresentation representation) {
        if (! companyId.equals(representation.id)) {
            throw new RuntimeException("[!] PUT /company\n\tCould not update company, id mismatching");
            //return Response.status(400).build();
        }

        Company company = companyMapper.toModel(representation);
        companyRepository.getCompanyEntityManager().merge(company);
        return Response.noContent().build();
    }

    //@PUT
    //@Transactional
    //@Path("/{companyId:[0-9]+}/updatePolicy")
    public Response updatePolicy(@PathParam("companyId") Integer companyId, ChargingPolicyRepresentation policyRepresentation) {
        if (companyId == null || companyRepository.findByCompanyIdOptional(companyId).isEmpty()) {
            throw new NotFoundException("[!] PUT /company/"+companyId+"/updatePolicy\n\tCould not find company, invalid id");
        }
//       todo des charging Policy resource
//        if (policyRepresentation.id == null || policyRepository.findByIdOptional(policyRepresentation.id).isEmpty()) {
//            throw new NotFoundException("[!] PUT /company/"+companyId+"/updatePolicy\n\tCould not find policy, invalid id");
//        }

        Company company = companyRepository.findByCompanyIdOptional(companyId)
                .orElseThrow(() -> new NotFoundException("[!] PUT /company/"+companyId+"/updatePolicy\n\tThis policy does not belong to the specified company"));

        ChargingPolicy policy = policyMapper.toModel(policyRepresentation);
        company.setPolicy(policy);
        companyRepository.getCompanyEntityManager().merge(company);
        return Response.status(Response.Status.OK).build();
    }


    // ---------- POST ----------

    @POST
    @Path("{companyId}: [0-9]+}/calculateCosts")
    @Transactional
    public HashMap<String, Float> getAllCosts(
            @QueryParam("miles") float miles,
            @QueryParam("damageType") DamageType damageType,
            @PathParam("companyId") Integer id ) {
        return null;
    }

    // ---------- DELETE ----------

    @DELETE
    @Transactional
    public Response deleteAllCompanies() {
        companyRepository.deleteAllCompanies();
        return Response.status(200).build();
    }

    @DELETE
    @Transactional
    @Path("{companyId: [0-9]+}")
    public Response deleteCompany(@PathParam("companyId") Integer companyId) {
        if (companyId == null || companyRepository.findByCompanyIdOptional(companyId).isEmpty()) {
            throw new NotFoundException("[!] DELETE /company" + companyId + "\n\tCould not find company with id " + companyId);
        }

        companyRepository.deleteCompany(companyId);
        boolean deleted = companyRepository.findByCompanyIdOptional(companyId).isEmpty();
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /company" + companyId + "\n\tCould not delete company with id " + companyId);
        }
        return Response.status(200).build();
    }

}
