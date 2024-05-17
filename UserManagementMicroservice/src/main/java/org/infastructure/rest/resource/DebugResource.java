package org.infastructure.rest.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.util.Debug;

@Path("/debug")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class DebugResource {

    @GET
    public Response getDebugInfo() {

        return Response.status(Response.Status.OK).entity("Debug mode: " + Debug.debug + "\n"
                + "Delay in ms: " + Debug.delay).build();
    }

    @POST
    public Response setDebugInfo(@DefaultValue("true") @QueryParam("debug") boolean debug,
                                 @DefaultValue("0") @QueryParam("delay") int delay) {
        Debug.debug = debug;
        Debug.delay = delay;
        return Response.status(Response.Status.OK).build();
    }
}
