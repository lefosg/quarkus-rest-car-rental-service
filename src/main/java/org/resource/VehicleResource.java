package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.domain.Vehicle;
import org.persistence.VehicleRepository;
import org.representation.CompanyMapper;
import org.representation.CompanyRepresentation;
import org.representation.VehicleMapper;
import org.representation.VehicleRepresentation;

import java.net.URI;
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

    // ---------- GET ----------

    @GET
    @Transactional
    public List<VehicleRepresentation> listAllVehicles(@DefaultValue("") @QueryParam("manufacturer") String manufacturer) {
        return vehicleMapper.toRepresentationList(vehicleRepository.findByManufacturer(manufacturer));
    }

    @GET
    @Path("{vehicleId: [0-9]+}")
    @Transactional
    public VehicleRepresentation listVehicleById(@PathParam("vehicleId") String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(Integer.parseInt(vehicleId));
        if (vehicle == null) {
            throw new NotFoundException("[!] GET /vehicle/"+vehicleId+"\n\tCould not find vehicle with id " + vehicleId);
        }
        return vehicleMapper.toRepresentation(vehicle);
    }

    @GET
    @Path("{vehicleId: [0-9]+}/company")
    @Transactional
    public CompanyRepresentation listCompanyOfVehicle(@PathParam("vehicleId") String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(Integer.parseInt(vehicleId));
        if (vehicle == null) {
            throw new NotFoundException("[!] GET /vehicle/"+vehicleId+"\n\tCould not find vehicle with id " + vehicleId);
        }
        return companyMapper.toRepresentation(vehicle.getCompany());
    }

    // ---------- PUT ----------

    //@PUT
    //@Transactional
    public Response create(VehicleRepresentation representation) {
        if (representation.id == null || vehicleRepository.findById(representation.id) != null) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /vehicle\n\tCould not create vehicle, invalid id");
        }

        Vehicle vehicle = vehicleMapper.toModel(representation);
        vehicleRepository.persist(vehicle);
        URI uri = UriBuilder.fromResource(VehicleResource.class).path(String.valueOf(vehicle.getId())).build();
        return Response.created(uri).entity(vehicleMapper.toRepresentation(vehicle)).build();
    }

    @PUT
    @Path("{vehicleId:[0-9]+}")
    @Transactional
    public Response update(@PathParam("vehicleId") Integer vehicleId, VehicleRepresentation representation) {
        if (vehicleId == null || !(vehicleId).equals(representation.id)) {
            throw new NotFoundException("[!] PUT /vehicle\n\tCould not update vehicle, id mismatching");
        }

        Vehicle vehicle = vehicleMapper.toModel(representation);
        vehicleRepository.getEntityManager().merge(vehicle);
        return Response.noContent().build();
    }

    // ---------- DELETE ----------

    @DELETE
    @Transactional
    public Response deleteAll() {
        vehicleRepository.deleteAllVehicles();
        return Response.status(200).build();
    }

    @DELETE
    @Path("{vehicleId: [0-9]+}")
    @Transactional
    public Response deleteVehicle(@PathParam("vehicleId") Integer vehicleId) {
        if (vehicleId == null || vehicleRepository.findById(vehicleId) == null) {
            throw new NotFoundException("[!] DELETE /vehicle" + vehicleId + "\n\tCould not delete vehicle with id " + vehicleId + "(id not found)");
        }

        vehicleRepository.deleteVehicle(vehicleId);
        boolean deleted = vehicleRepository.findById(vehicleId) == null;
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /vehicle" + vehicleId + "\n\tCould not delete vehicle with id " + vehicleId);
        }
        return Response.status(200).build();
    }

}
