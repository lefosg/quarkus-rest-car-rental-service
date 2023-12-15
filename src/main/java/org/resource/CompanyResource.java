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

    @GET
    @Transactional
    public List<CompanyRepresentation> listAllCompanies() {
        return companyMapper.toRepresentationList(companyRepository.listAll());
    }

    @GET
    @Path("{companyId}")  //fixme? regex
    @Transactional
    public CompanyRepresentation listCompanyById(@PathParam("companyId") String companyId) {
        return companyMapper.toRepresentation(companyRepository.findById(Integer.parseInt(companyId)));
    }

}
