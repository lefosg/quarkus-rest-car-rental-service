package org.infastructure.rest.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.application.UserService;
import org.domain.company.ChargingPolicy;
import org.domain.company.ChargingPolicyRepository;
import org.domain.company.Company;
import org.domain.company.CompanyRepository;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.infastructure.rest.representation.*;
import org.infastructure.service.fleetManagament.representation.VehicleRepresentation;

import org.util.Constants;
import org.util.DamageType;
import org.util.Debug;


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
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 10)
    @Counted(name = "countListAllCompanies", description = "Count how many times listAllCompanies has been called")
    @Timed(name = "timeListAllCompanies", description = "How long it takes to invoke listAllCompanies")
    public List<CompanyRepresentation> listAllCompanies(@DefaultValue("") @QueryParam("city") String city) {
        return companyMapper.toRepresentationList(companyRepository.findByCity(city));  //find by city will check for empty strings etc
    }

    @GET
    @Transactional
    @Path("{companyId: [0-9]+}")
    public CompanyRepresentation listCompanyById(@PathParam("companyId") Integer companyId) {
       Company company = companyRepository.findByCompanyIdOptional(companyId)
               .orElseThrow(() -> new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company with id " + companyId));

        return companyMapper.toRepresentation(company);
    }

    @GET
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 4)
    @Counted(name = "countListCompanyVehicles", description = "Count how many times listCompanyVehicles has been called")
    @Timed(name = "timeListCompanyVehicles", description = "How long it takes to invoke listCompanyVehicles")
    @Path("/{companyId: [0-9]+}/vehicles")
    @Transactional
    public List<VehicleRepresentation> listCompanyVehicles(@PathParam("companyId") Integer companyId) {
        Debug.delay();

        Company company = companyRepository.findByCompanyIdOptional(companyId)
            .orElseThrow(() -> new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company with id " + companyId));
        return userService.getFleet(companyId);
    }

    @GET
    @Transactional
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 4)
    @Path("{companyId: [0-9]+}/policy")
    public ChargingPolicyRepresentation listCompanyPolicy(@PathParam("companyId") Integer companyId) {
        Debug.delay();

        Company company = companyRepository.findCompanyById(companyId);

        if (company ==  null) {
            throw new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company");}
        return policyMapper.toRepresentation(company.getPolicy());
    }

    // ---------- PUT ----------

    @PUT
    @Transactional
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 5)
    @Timed(name = "timeCreateCompany", description = "How long it takes to invoke createCompany")
    public Response create(CompanyRepresentation representation) {
        Debug.delay();

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
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 5)
    @Counted(name = "countUpdateCompany", description = "Count how many times updateCompany has been called")
    @Timed(name = "timeUpdateCompany", description = "How long it takes to invoke updateCompany")
    @Path("/{companyId:[0-9]+}")
    public Response update(@PathParam("companyId") Integer companyId, CompanyRepresentation representation) {
        Debug.delay();

        if (! companyId.equals(representation.id)) {
            throw new RuntimeException("[!] PUT /company\n\tCould not update company, id mismatching");
            //return Response.status(400).build();
        }

        Company company = companyMapper.toModel(representation);
        companyRepository.getCompanyEntityManager().merge(company);
        return Response.noContent().build();
    }

    // ---------- POST ----------

    @POST
    @Transactional
    @CircuitBreaker(requestVolumeThreshold = 10, delay = 10000, successThreshold = 5)
    @Counted(name = "countGetAllCosts", description = "Count how many times getAllCosts has been called")
    @Timed(name = "timeGetAllCosts", description = "How long it takes to invoke getAllCosts")
    @Metered(name = "meteredGetAllCosts", description = "Measures throughput of getAllCosts method")
    //@Gauge(name = "gaugeGetAllCosts", description = "Time of getAllCosts method", unit = MetricUnits.NONE)
    @Path("/calculateCosts/{companyId: [0-9]+}")
    public HashMap<String, Float> getAllCosts(
            @PathParam("companyId") Integer companyId,
            @QueryParam("miles") float miles,
            @QueryParam("damageType") DamageType damageType ) {

        if (miles <= 0) {
            throw new RuntimeException("[!] Company.getAllCosts: miles was <= 0");}
        if (damageType == null) {
            throw new RuntimeException("[!] Company.getAllCosts: damageType was null");}
        Company company = companyRepository.findByCompanyIdOptional(companyId)
                .orElseThrow(() -> new NotFoundException("[!] Company.getAllCosts: company not found"));
        ChargingPolicy policy = company.getPolicy();

        Float damage_cost = policy.calculateDamageCost(damageType);
        Float mileage_cost = policy.calculateMileageCost(miles);
        HashMap<String, Float> costs = new HashMap<>();
        costs.put(Constants.damageCost, damage_cost);
        costs.put(Constants.mileageCost, mileage_cost);
        return costs;
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
