package org.infastructure.rest.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.application.RentService;
import org.common.BusinessRuleException;
import org.domain.Rents.Rent;
import org.domain.Rents.RentRepository;
import org.domain.TechnicalCheck.TechnicalCheck;
import org.domain.TechnicalCheck.TechnicalCheckRepository;
import org.infastructure.rest.representation.RentMapper;
import org.infastructure.rest.representation.RentRepresentation;
import org.infastructure.rest.representation.TechnicalCheckMapper;
import org.infastructure.rest.representation.TechnicalCheckRepresentation;
import org.infastructure.service.fleet.representation.VehicleRepresentation;
import org.infastructure.service.userManagement.representation.CustomerRepresentation;
import org.util.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.infastructure.rest.ApiPath.Root.RENTS;


@Path(RENTS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class RentResource {

    @Inject
    RentRepository rentRepository;

    @Inject
    RentMapper rentMapper;

    @Inject
    RentService rentService;

    @Inject
    TechnicalCheckMapper technicalCheckMapper;

    @Inject
    TechnicalCheckRepository technicalCheckRepository;

    @Context
    UriInfo uriInfo;

    // ---------- GET ----------

    @GET
    @Transactional
    public List<RentRepresentation> listAllRents(@DefaultValue("0") @QueryParam("month") Integer month) {
        return rentMapper.toRepresentationList(rentRepository.findByMonth(month));
    }

    @GET
    @Transactional
    @Path("{rentId: [0-9]+}")
    public RentRepresentation listRentById(@PathParam("rentId") Integer rentId) {
        Rent rent = rentRepository.findRentById(rentId);
        if (rent == null) {
            throw new NotFoundException("[!] GET /rent/"+rentId+"\n\tCould not find rent with id " + rentId);
        }

        return rentMapper.toRepresentation(rent);
    }
    //todo: add /rent/rentid/policy

// todo all these fpr USer fleet

    @GET
    @Transactional
    @Path("{rentId: [0-9]+}/customer")
    public CustomerRepresentation listCustomerOfRent(@PathParam("rentId") Integer rentId) {
        if (rentId == null) {
            throw new NotFoundException("[!] GET /rent/null/customer"+"\n\tCould not find rent with id null");
        }
        Rent rent = rentRepository.findRentByIdOptional(rentId)
                .orElseThrow(() -> new NotFoundException("[!] GET /rent/"+rentId+"\n\tCould not find rent with id " + rentId));

        return rentService.returnCustomerWithId(rent.getCustomerId());
    }

    @GET
    @Transactional
    @Path("{rentId: [0-9]+}/vehicle")
    public VehicleRepresentation listVehicleOfRent(@PathParam("rentId") Integer rentId) {
        if (rentId == null) {
            throw new NotFoundException("[!] GET /rent/null/vehicle"+"\n\tCould not find rent with id null");
        }
        Rent rent = rentRepository.findRentByIdOptional(rentId)
                .orElseThrow(() -> new NotFoundException("[!] GET /rent/"+rentId+"\n\tCould not find rent with id " + rentId));

        return rentService.returnVehicleWithId(rent.getVehicleId());
    }

    @GET
    @Transactional
    @Path("{rentId: [0-9]+}/technicalCheck")
    public TechnicalCheckRepresentation listTechnicalCheckOfRent(@PathParam("rentId") Integer rentId) {
        if (rentId == null) {
            throw new NotFoundException("[!] GET /rent/null/technicalCheck"+"\n\tCould not find rent with id null");
        }
        Rent rent = rentRepository.findRentByIdOptional(rentId)
                .orElseThrow(() -> new NotFoundException("[!] GET /rent"+rentId+"/policy/\n\tCould not find policy with id " + rentId));

        return technicalCheckMapper.toRepresentation(rent.getTechnicalCheck());
    }

    // ---------- POST ----------

//    @PUT
//    @Transactional
    public Response create(RentRepresentation representation) {
        if (!rentService.rentedVehicleExist(representation.vehicleId)) {
            System.out.println("Vehicle does not exist");
            throw new NotFoundException("[!] PUT /rent\n\tCould not create rent, invalid Vehicle id");
        }
        if (!rentService.customerExist(representation.customerId)) {
            System.out.println("Customer does not exist");
            throw new NotFoundException("[!] PUT /rent\n\tCould not create rent, invalid Customer id");
        }
        if (representation.id == null || rentRepository.findRentByIdOptional(representation.id).isPresent()) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /policy\n\tCould not create rent, invalid id");
        }
        Rent rent = rentMapper.toModel(representation);
        rentRepository.persistRent(rent);
        URI uri = UriBuilder.fromResource(RentResource.class).path(String.valueOf(rent.getId())).build();
        return Response.created(uri).entity(rentMapper.toRepresentation(rent)).build();
    }

    @POST
    @Transactional
    @Path("/newRent/{customerId:[0-9]+}")
    public Response makeRent(
            @QueryParam("startDate") String start,
            @QueryParam("endDate") String end,
            @QueryParam("vehicleId") Integer vehicleId,
            @PathParam("customerId") Integer customerId) {

        LocalDate startDate = null;
        LocalDate endDate = null;
        if ((start != null && end != null) && (!start.equals("") && !end.equals("")) && LocalDate.parse(start).isBefore(LocalDate.parse(end))) {
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid date parameters").build();
        }
        if (customerId == null || customerId.describeConstable().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid customer id").build();
        }
        if (vehicleId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid vehicle id").build();
        }

        try {
            CustomerRepresentation customer = rentService.returnCustomerWithId(customerId);
            if (customer.id == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Customer does not exist").build();
            }
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer does not exist").build();
        }
        try {
            VehicleRepresentation vehicle = rentService.returnVehicleWithId(vehicleId);
            if (vehicle.vehicleState != VehicleState.Available) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Vehicle not available until " + end).build();
            }
        } catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Vehicle does not exist").build();
        }

        Rent rent = new Rent(rentRepository.findMaxId()+1,startDate, endDate,vehicleId,customerId);

        rentService.makeVehicleRented(vehicleId);
        rentRepository.persistRent(rent);
        return Response.status(Response.Status.OK).entity("You rented the vehicle").build();
    }

    @POST
    @Transactional
    @Path("/returnVehicle/{customerId:[0-9]+}")
    public Response returnVehicle(
            @PathParam("customerId") Integer customerId,
            @QueryParam("vehicleId") Integer vehicleId,
            @QueryParam("miles") float miles
    ) {

        CustomerRepresentation customer = rentService.returnCustomerWithId(customerId);
        if (customerId == null || customer.id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer does not exist or id was null").build();
        }

        VehicleRepresentation vehicle = rentService.returnVehicleWithId(vehicleId);
        if (vehicleId == null || vehicle.id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Vehicle does not exist or id was null").build();
        }
        if (miles <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Miles provided are negative").build();
        }

        if (vehicle.vehicleState == VehicleState.Available) {
            return Response.status(Response.Status.BAD_REQUEST).entity("This vehicle cannot be returned").build();
        }

        Rent rent = rentService.findRent(customerId, vehicleId);
        //1 costs calculation
        HashMap<String, Float> costs = rentService.calculateCosts(customerId, vehicleId, miles);
        double fixedCost = vehicle.fixedCharge.getAmount() * rent.getDurationInDays();
        costs.put(Constants.fixedCost, (float)fixedCost);
        Money totalCosts = new Money(costs.get(Constants.damageCost) + costs.get(Constants.mileageCost) + fixedCost);
        Money damageCosts = new Money(costs.get(Constants.damageCost));

        //2 Update Rent: state = Finished
        rent.setRentState(RentState.Finished);
        //3 Update Vehicle: setVehicleState = available AND setVehicle Miles
        //todo vaggelh kane thn super synarthsh poy ta kanei ola se ena gia ayto
        //4 payment
        boolean isPayed = rentService.pay(customerId,vehicleId, totalCosts.getAmount(), damageCosts.getAmount());
        if(!isPayed){
            throw new BusinessRuleException("Something went wrong with payment");
        }

        rentRepository.getEntityManagerRent().merge(rent);
        return Response.status(Response.Status.OK).entity("Vehicle returned").build();
    }

    // ---------- PUT ----------

    @PUT
    @Transactional
    @Path("{rentId: [0-9]+}")
    public Response update(@PathParam("rentId") Integer rentId, RentRepresentation representation) {
        if (rentId == null || !(rentId).equals(representation.id)) {
            throw new NotFoundException("[!] PUT /rent\n\tCould not update rent, id mismatching");
        }
        Rent rent = rentMapper.toModel(representation);
        rentRepository.getEntityManagerRent().merge(rent);
        return Response.noContent().build();
    }

    // ---------- DELETE ----------

    @DELETE
    @Transactional
    public Response deleteAllRents() {
        rentRepository.deleteAllRents();
        return Response.status(200).build();
    }

    @DELETE
    @Transactional
    @Path("{rentId: [0-9]+}")
    public Response deleteCompany(@PathParam("rentId") Integer rentId) {
        if (rentId == null || rentRepository.findRentByIdOptional(rentId).isEmpty()) {
            throw new NotFoundException("[!] DELETE /rent" + rentId + "\n\tCould not find rent with id " + rentId);
        }

        rentRepository.deleteRent(rentId);
        boolean deleted = rentRepository.findRentByIdOptional(rentId).isEmpty();
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /rent" + rentId + "\n\tCould not delete rent with id " + rentId);
        }
        return Response.status(200).build();
    }

}
