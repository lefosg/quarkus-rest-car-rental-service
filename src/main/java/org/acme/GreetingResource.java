package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Date;
import org.entities.Customer;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Date lalala() {
        Date date = new Date();
        Customer customer=new Customer("GObbb","GObbb",date,"GObbb","GObbb","GObbb","GObbb","GObbb","GObbb","GObbb");
        return customer.getExpirationDate();
    }
}
