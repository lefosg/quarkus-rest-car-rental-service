package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.domain.Company;
import org.domain.Vehicle;
import org.persistence.VehicleRepository;
import org.representation.CompanyMapper;
import org.representation.CompanyRepresentation;
import org.representation.VehicleMapper;
import org.representation.VehicleRepresentation;

import java.util.List;

@Path("vehicle")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class VehicleResource {

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleMapper vehicleMapper;

    @Inject
    CompanyMapper companyMapper;

    @Context
    UriInfo uriInfo;

    @GET
    @Transactional
    public List<VehicleRepresentation> listAllVehicles() {
        return vehicleMapper.toRepresentationList(vehicleRepository.listAll());
    }

    @GET
    @Path("{vehicleId: [0-9]+}")
    @Transactional
    public VehicleRepresentation listVehicleById(@PathParam("vehicleId") Integer vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle == null) {
            throw new NotFoundException("[!] GET /vehicle/"+vehicleId+"\n\tCould not find company with id " + vehicleId);
        }
        return vehicleMapper.toRepresentation(vehicle);
    }

   // @GET
   // @Path("{vehicleId}:[0-9]+/companies")
    //@Transactional
   // public List<VehicleRepresentation> listVehiclesCompany(@PathParam("vehicleId") String vehicleId) {
     //   Vehicle vehicle = vehicleRepository.findById(Integer.parseInt(vehicleId));

     //   if (vehicle ==  null) {
       //     throw new NotFoundException("[!] GET /vehicle/"+vehicleId+"\n\tCould not find company");
     //   }
     //   return companyMapper.toRepresentationList(vehicle.getCompanies());
  //  }
}
