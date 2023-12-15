package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.domain.Company;
import org.persistence.CompanyRepository;
import org.representation.CompanyMapper;
import org.representation.CompanyRepresentation;
import org.representation.VehicleMapper;
import org.representation.VehicleRepresentation;

import java.net.URI;
import java.util.List;

@Path("company")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class CompanyResource {

    @Inject
    CompanyRepository companyRepository;

    @Inject
    CompanyMapper companyMapper;

    @Inject
    VehicleMapper vehicleMapper;

    @Context
    UriInfo uriInfo;

    // ---------- GET ----------

    @GET
    @Transactional
    public List<CompanyRepresentation> listAllCompanies() {
        return companyMapper.toRepresentationList(companyRepository.listAll());
    }

    @GET
    @Path("{companyId: [0-9]+}")
    @Transactional
    public CompanyRepresentation listCompanyById(@PathParam("companyId") Integer companyId) {
        Company company = companyRepository.findById(companyId);

        if (company ==  null) {
            throw new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company with id " + companyId);
        }
        return companyMapper.toRepresentation(company);
    }

//    @GET  //fixme
//    @Path("{city: [A-Z]+}")
//    @Transactional
//    public List<CompanyRepresentation> listCompaniesByCity(@PathParam("city") String city) {
//        List<Company> companies = companyRepository.findByCity(city);
//        if (companies == null) {
//            throw new NotFoundException("[!] GET /company/"+city+"\n\tCould not find companies in city " + city);
//        }
//
//        return companyMapper.toRepresentationList(companies);
//    }

    @GET
    @Path("{companyId}:[0-9]+/vehicles")
    @Transactional
    public List<VehicleRepresentation> listCompanyVehicles(@PathParam("companyId") String companyId) {
        Company company = companyRepository.findById(Integer.parseInt(companyId));

        if (company ==  null) {
            throw new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company");
        }
        return vehicleMapper.toRepresentationList(company.getVehicles());
    }

    // ---------- PUT ----------

    @PUT
    @Transactional
    public Response create(CompanyRepresentation representation) {
        if (representation.id == null || companyRepository.findById(representation.id) != null) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /company\n\tCould not create company, id not found");
        }

        Company company = companyMapper.toModel(representation);
        companyRepository.persist(company);
        URI uri = UriBuilder.fromResource(CompanyResource.class).path(String.valueOf(company.getId())).build();
        return Response.created(uri).entity(companyMapper.toRepresentation(company)).build();
    }

    @PUT
    @Transactional
    @Path("/{companyId:[0-9]+}")
    public Response update(@PathParam("companyId") Integer companyId, CompanyRepresentation representation) {
        if (! companyId.equals(representation.id)) {
            throw new RuntimeException("[!] PUT /company\n\tCould not update company, id mismatching");
        }

        Company company = companyMapper.toModel(representation);
        companyRepository.getEntityManager().merge(company);
        return Response.noContent().build();
    }

}
