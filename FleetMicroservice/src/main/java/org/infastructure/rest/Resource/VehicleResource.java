package org.infastructure.rest.Resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.application.VehicleService;
import org.domain.Vehicle.Vehicle;
import org.domain.Vehicle.VehicleRepository;
import org.infastructure.rest.ApiPath;
import org.infastructure.rest.Representation.VehicleMapper;
import org.infastructure.rest.Representation.VehicleRepresentation;
import org.infastructure.service.user_management.representation.CompanyRepresentation;

import java.net.URI;
import java.util.List;

@Path(ApiPath.Root.VEHICLE)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class VehicleResource {

    @Inject
    VehicleRepository vehicleRepository;

    @Inject
    VehicleMapper vehicleMapper;

    @Inject
    VehicleService vehicleService;

    @Context
    UriInfo uriInfo;

    // ---------- GET ----------

    @GET
    @Transactional
    public List<VehicleRepresentation> listAllVehicles(
            //@DefaultValue("") @QueryParam("manufacturer") String manufacturer,
            //@DefaultValue("") @QueryParam("vehicleState") VehicleState vehicleState
    ) {
        //if query parameters are empty strings, return all vehicles
        //if (manufacturer.equals("") && vehicleState.equals("")) {
            return vehicleMapper.toRepresentationList(vehicleRepository.listAllVehicles());
        //}
        //return vehicleMapper.toRepresentationList(vehicleRepository.findByManufacturerAndState(manufacturer, vehicleState));
    }

    @GET
    @Path("{vehicleId: [0-9]+}")
    @Transactional
    public VehicleRepresentation listVehicleById(@PathParam("vehicleId") String vehicleId) {
        Vehicle vehicle = vehicleRepository.findVehicleByIdOptional(Integer.parseInt(vehicleId))
                .orElseThrow(() -> new NotFoundException("[!] GET /vehicle/"+vehicleId+"\n\tCould not find vehicle with id " + vehicleId));

        return vehicleMapper.toRepresentation(vehicle);
    }

    @GET
    @Path("/vehiclesByCompany/{companyId: [0-9]+}")
    @Transactional
    public List<VehicleRepresentation> listVehiclesByCompanyId(@PathParam("companyId") Integer companyId) {
        List<Vehicle> vehicles = vehicleRepository.findVehiclesByCompanyId(companyId);
                //.orElseThrow(() -> new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company with id " + companyId));
        return vehicleMapper.toRepresentationList(vehicles);
    }

    @GET
    @Path("{vehicleId: [0-9]+}/company")
    @Transactional
    public CompanyRepresentation listCompanyOfVehicle(@PathParam("vehicleId") String vehicleId) {
        //1. find the vehicle
        Vehicle vehicle = vehicleRepository.findVehicleByIdOptional(Integer.parseInt(vehicleId))
                .orElseThrow(() -> new NotFoundException("[!] GET /vehicle/"+vehicleId+"\n\tCould not find vehicle with id " + vehicleId));
        //2. check if the company of the vehicle exists
        try {
            return vehicleService.getCompany(vehicle.getCompanyId());
        } catch (Exception e ) {
            //e.printStackTrace();
            return new CompanyRepresentation();
        }
    }

    // ---------- PUT ----------

    @PUT
    @Transactional
    public Response create(VehicleRepresentation representation) {
        //first check if company id exists
        if (!vehicleService.companyExists(representation.companyId)) {
            System.out.println("company does not exist");
            throw new NotFoundException("[!] PUT /vehicle\n\tCould not create vehicle, invalid company id");
        }
        if (representation.id == null || vehicleRepository.findVehicleByIdOptional(representation.id).isPresent()) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /vehicle\n\tCould not create vehicle, invalid id");
        }
        Vehicle vehicle = vehicleMapper.toModel(representation);
        vehicleRepository.persistVehicle(vehicle);
        URI uri = UriBuilder.fromResource(VehicleResource.class).path(String.valueOf(vehicle.getId())).build();
        return Response.created(uri).entity(vehicleMapper.toRepresentation(vehicle)).build();
    }

    @PUT
    @Path("{vehicleId:[0-9]+}")
    @Transactional
    public Response update(@PathParam("vehicleId") Integer vehicleId, VehicleRepresentation representation) {
        //first check if company id exists
        if (!vehicleService.companyExists(representation.companyId)) {
            System.out.println("company does not exist");
            throw new NotFoundException("[!] PUT /vehicle\n\tCould not create vehicle, invalid company id");
        }
        if (vehicleId == null || !(vehicleId).equals(representation.id)) {
            throw new NotFoundException("[!] PUT /vehicle\n\tCould not update vehicle, id mismatching");
        }

        Vehicle vehicle = vehicleMapper.toModel(representation);
        vehicleRepository.getVehicleEntityManager().merge(vehicle);
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
        if (vehicleId == null || vehicleRepository.findVehicleByIdOptional(vehicleId).isEmpty()) {
            throw new NotFoundException("[!] DELETE /vehicle" + vehicleId + "\n\tCould not delete vehicle with id " + vehicleId + "(id not found)");
        }

        vehicleRepository.deleteVehicle(vehicleId);
        boolean deleted = vehicleRepository.findVehicleByIdOptional(vehicleId).isEmpty();
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /vehicle" + vehicleId + "\n\tCould not delete vehicle with id " + vehicleId);
        }
        return Response.status(200).build();
    }

}