package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.domain.Company;
import org.persistence.CompanyRepository;
import org.representation.CompanyMapper;
import org.representation.CompanyRepresentation;
import org.representation.VehicleMapper;
import org.representation.VehicleRepresentation;

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
    @Path("{companyId}/vehicles")
    @Transactional
    public List<VehicleRepresentation> listCompanyVehicles(@PathParam("companyId") String companyId) {
        Company company = companyRepository.findById(Integer.parseInt(companyId));

        if (company ==  null) {
            throw new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company");
        }
        return vehicleMapper.toRepresentationList(company.getVehicles());
    }

    // ---------- PUT ----------


}
