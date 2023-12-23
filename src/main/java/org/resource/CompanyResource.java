package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.domain.Company;
import org.domain.Vehicle;
import org.persistence.CompanyRepository;
import org.persistence.VehicleRepository;
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

    @Inject
    VehicleRepository vehicleRepository;

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
        Company company = companyRepository.findById(companyId);

        if (company ==  null) {
            throw new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company with id " + companyId);
        }
        return companyMapper.toRepresentation(company);
    }

    @GET
    @Path("{companyId: [0-9]+}/vehicles")
    @Transactional
    public List<VehicleRepresentation> listCompanyVehicles(@PathParam("companyId") Integer companyId) {
        Company company = companyRepository.findById(companyId);

        if (company ==  null) {
            throw new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company");
        }
        return vehicleMapper.toRepresentationList(company.getVehicles());
    }

    // ---------- PUT ----------

    @PUT
    @Transactional
    public Response create(CompanyRepresentation representation) {
        if (representation.id == null ||  companyRepository.findById(representation.id) != null ) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /company\n\tCould not create company, invalid id");
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
            //return Response.status(400).build();
        }

        Company company = companyMapper.toModel(representation);
        companyRepository.getEntityManager().merge(company);
        return Response.noContent().build();
    }


    // ---------- POST ----------

    @POST
    @Transactional
    @Path("{companyId:[0-9]+}/addVehicle")
    public Response addVehicle(@PathParam("companyId") Integer companyId, VehicleRepresentation vehicleRepresentation) {
        if (companyId == null || companyRepository.findById(companyId) == null) {
            throw new NotFoundException("[!] POST /company/"+companyId+"/addVehicle\n\tCould not find company, invalid id");
        }
        if (vehicleRepresentation.id == null || vehicleRepository.findById(vehicleRepresentation.id) != null) {
            throw new NotFoundException("[!] POST /company/"+companyId+"/addVehicle\n\tCould not find vehicle or already exists");
        }
        if (!companyId.equals(vehicleRepresentation.company)) {  //company calling this endpoint must be same in companyId and representation.company
            throw new NotFoundException("[!] POST /company/"+companyId+"/addVehicle\n\trepresentation.company is not the same as companyId ("+companyId+")");
        }
        Vehicle vehicle = vehicleMapper.toModel(vehicleRepresentation);
        Company company = companyRepository.findById(companyId);
        company.addVehicle(vehicle);
        companyRepository.getEntityManager().merge(company);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(vehicle.getId())).build();
        return Response.created(uri).entity(vehicleMapper.toRepresentation(vehicle)).status(Response.Status.OK).build();
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
        if (companyId == null || companyRepository.findById(companyId) == null) {
            throw new NotFoundException("[!] DELETE /company" + companyId + "\n\tCould not find company with id " + companyId);
        }

        companyRepository.deleteCompany(companyId);
        boolean deleted = companyRepository.findById(companyId) == null;
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /company" + companyId + "\n\tCould not delete company with id " + companyId);
        }
        return Response.status(200).build();
    }

}
