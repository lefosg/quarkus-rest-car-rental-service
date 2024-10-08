package org.infastructure.rest.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.domain.company.Company;
import org.domain.company.CompanyRepository;
import org.domain.customer.Customer;
import org.domain.customer.CustomerRepository;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.infastructure.rest.representation.CustomerMapper;
import org.infastructure.rest.representation.CustomerRepresentation;
import org.util.Debug;
import org.util.Money;
import java.net.URI;
import java.util.List;
import static org.infastructure.rest.ApiPath.Root.CUSTOMER;

@Path(CUSTOMER)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class CustomerResource {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    CustomerMapper customerMapper;

    @Inject
    CompanyRepository companyRepository;

    // ---------- GET ----------

    @GET
    @Transactional
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 5)
    @Counted(name = "countListAllCustomers", description = "Count how many times listAllCustomers has been called")
    @Timed(name = "timeListAllCustomers", description = "How long it takes to invoke listAllCustomers")
    public List<CustomerRepresentation> listAllCustomers() {
        Debug.delay();
        return customerMapper.toRepresentationList(customerRepository.listAllCustomers());
    }

    @GET
    @Transactional
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 5)
    @Path("/{customerId: [0-9]+}")
    public CustomerRepresentation listCustomerById(@PathParam("customerId") Integer customerId) {
        Debug.delay();

        Customer customer = customerRepository.findByCustomerIdOptional(customerId)
                .orElseThrow(() -> new NotFoundException("[!] GET /customer/"+customerId+"\n\tCould not find customer with id " + customerId));
        return customerMapper.toRepresentation(customer);
    }

    // ---------- PUT ----------
    @PUT
    @Transactional
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 5)
    @Timed(name = "timeCreateCustomer", description = "How long it takes to invoke createCustomer")
    public Response create(CustomerRepresentation representation) {
        Debug.delay();

        if (representation.id == null || customerRepository.findByCustomerIdOptional(representation.id).isPresent()) {  //if id is null or already exists
            throw new NotFoundException("[!] PUT /customer\n\tCould not create customer, invalid id");
        }
        Customer customer = customerMapper.toModel(representation);
        customerRepository.persistCustomer(customer);
        URI uri = UriBuilder.fromResource(CustomerResource.class).path(String.valueOf(customer.getId())).build();
        return Response.created(uri).entity(customerMapper.toRepresentation(customer)).build();
    }

    @PUT
    @Transactional
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 5)
    @Counted(name = "countUpdateCustomer", description = "Count how many times updateCustomer has been called")
    @Timed(name = "timeUpdateCustomer", description = "How long it takes to invoke updateCustomer")
    @Path("/{customerId:[0-9]+}")
    public Response update(@PathParam("customerId") Integer customerId, CustomerRepresentation representation) {
        Debug.delay();

        if (! customerId.equals(representation.id)) {
            throw new RuntimeException("[!] PUT /customer " + customerId + "\n\tCould not update customer, id mismatching");
        }

        Customer customer = customerMapper.toModel(representation);
        customerRepository.getCustomerEntityManager().merge(customer);
        return Response.noContent().build();
    }

    // ---------- POST ----------

     @POST
     @Transactional
     @CircuitBreaker(requestVolumeThreshold = 10, delay = 10000, successThreshold = 5)
     @Counted(name = "countPay", description = "Count how many times pay has been called")
     @Timed(name = "timePay", description = "How long it takes to invoke pay")
     @Metered(name = "meteredPay", description = "Measures throughput of pay method")
     //@Gauge(name = "gaugePay", description = "Time of pay method", unit = MetricUnits.NONE)
     @Path("/pay/{customerId: [0-9]+}/")
     public Response pay(@PathParam("customerId") Integer customerId,
                         @QueryParam("companyId") Integer companyId,
                         @QueryParam("amount_money") double amount_money,
                         @QueryParam("amount_damages") double amount_damages){

         Customer customer = customerRepository.findByCustomerIdOptional(customerId)
                 .orElseThrow(() -> new NotFoundException("[!] GET /customer/"+customerId+"\n\tCould not find customer with id " + customerId));

         Company company = companyRepository.findByCompanyIdOptional(companyId)
                 .orElseThrow(() -> new NotFoundException("[!] GET /company/"+companyId+"\n\tCould not find company with id " + companyId));

         try{
             customer.pay(new Money(amount_money),new Money(amount_damages),company);
         }catch (Exception e){
             throw new NullPointerException();
         }
         return Response.status(200).build();
    }

    // ---------- DELETE ----------

    @DELETE
    @Transactional
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 5)
    public Response deleteAllCustomers() {
        customerRepository.deleteAllCustomers();
        return Response.status(200).build();}

    @DELETE
    @Transactional
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Bulkhead(value = 5)
    @Path("{customerId: [0-9]+}")
    public Response deleteCustomer(@PathParam("customerId") Integer customerId) {
        if (customerId == null || customerRepository.findByCustomerIdOptional(customerId).isEmpty()) {
            return Response.status(404).build();
        }
        customerRepository.deleteCustomer(customerId);
        //todo nomizo oti kai edo den xreiazetai
        boolean deleted = customerRepository.findByCustomerIdOptional(customerId).isEmpty();
        if (!deleted) {
            throw new RuntimeException("[!] DELETE /customer " + customerId + "\n\tCould not delete customer with id " + customerId);
        }
        return Response.status(200).build();
    }

}

